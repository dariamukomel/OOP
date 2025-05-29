package ru.nsu.lavitskaya.mr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * MasterGateway handles discovery and communication with the Master server.
 * <p>Uses multicast for initial Master announcement, then establishes a TCP
 * connection for message exchange.</p>
 */
public class MasterGateway {
    private static final String MULTICAST_GROUP = "224.0.0.1";
    private static final int MULTICAST_PORT = 5000;
    private static final boolean CI =
            "true".equalsIgnoreCase(System.getenv("GITHUB_ACTIONS")) ||
                    "true".equalsIgnoreCase(System.getenv("CI"));

    private MulticastSocket multicastSocket;
    private Socket tcpSocket;
    private BufferedReader reader;
    private BufferedWriter writer;

    /**
     * discovers the Master via multicast and opens a TCP socket.
     */
    public void connect() throws IOException {
        InetAddress bindAddr = CI
                ? InetAddress.getLoopbackAddress()
                : InetAddress.getLocalHost();

        multicastSocket = new MulticastSocket(MULTICAST_PORT);

        NetworkInterface ni = NetworkInterface.getByInetAddress(bindAddr);
        if (ni == null) {
            ni = NetworkInterface.getByInetAddress(InetAddress.getLoopbackAddress());
        }
        if (ni == null) {
            throw new IOException("No network interface for multicast on " + bindAddr);
        }

        multicastSocket.setNetworkInterface(ni);

        InetAddress group = InetAddress.getByName(MULTICAST_GROUP);
        SocketAddress groupEP = new InetSocketAddress(group, MULTICAST_PORT);
        multicastSocket.joinGroup(groupEP, ni);

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        multicastSocket.receive(packet);

        String msg = new String(packet.getData(), 0, packet.getLength(),
                StandardCharsets.UTF_8)
                .trim();
        String[] parts = msg.split(":", 2);
        if (parts.length != 2) {
            throw new IOException("Invalid announcement: " + msg);
        }
        String masterHost = parts[0];
        int masterPort = Integer.parseInt(parts[1]);

        multicastSocket.leaveGroup(groupEP, ni);
        multicastSocket.close();

        tcpSocket = new Socket(masterHost, masterPort);
        reader = new BufferedReader(new InputStreamReader(
                tcpSocket.getInputStream(), StandardCharsets.UTF_8));
        writer = new BufferedWriter(new OutputStreamWriter(
                tcpSocket.getOutputStream(), StandardCharsets.UTF_8));
    }

    public Message getMessage() throws IOException {
        String raw = reader.readLine();
        return Message.parse(raw);
    }

    public void sendAnswer(String answer) throws IOException {
        writer.write(answer);
        writer.newLine();
        writer.flush();
    }

    public void close() {
        try {
            if (reader != null) reader.close();
        } catch (IOException ignored) {
        }
        try {
            if (writer != null) writer.close();
        } catch (IOException ignored) {
        }
        try {
            if (tcpSocket != null) tcpSocket.close();
        } catch (IOException ignored) {
        }
    }
}

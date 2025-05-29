package ru.nsu.lavitskaya.mr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * MasterGateway handles communication with the Master server.
 * <p>
 * Uses multicast for initial Master announcement, then establishes a TCP
 * connection for message exchange.</p>
 */
public class MasterGateway {
    private static final String MULTICAST_GROUP = "224.0.0.1";
    private static final int MULTICAST_PORT = 5000;

    private final boolean useLoopback;
    private MulticastSocket multicastSocket;
    private Socket tcpSocket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public MasterGateway(boolean useLoopback) {
        this.useLoopback = useLoopback;
    }

    public void connect() throws IOException {
        multicastSocket = new MulticastSocket(MULTICAST_PORT);
        InetAddress group = InetAddress.getByName(MULTICAST_GROUP);

        // выбираем интерфейс в зависимости от флага
        InetAddress addr = useLoopback
                ? InetAddress.getLoopbackAddress()
                : InetAddress.getLocalHost();

        NetworkInterface ni = NetworkInterface.getByInetAddress(addr);
        if (ni == null) {
            throw new IOException("Cannot find network interface for " + addr);
        }

        SocketAddress groupAddr = new InetSocketAddress(group, MULTICAST_PORT);
        multicastSocket.joinGroup(groupAddr, ni);

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        multicastSocket.receive(packet);

        String msg = new String(packet.getData(), 0, packet.getLength(),
                StandardCharsets.UTF_8).trim();
        String[] parts = msg.split(":", 2);
        if (parts.length != 2) {
            throw new IOException("Invalid master announcement: " + msg);
        }
        String masterHost = parts[0];
        int masterPort = Integer.parseInt(parts[1]);

        multicastSocket.leaveGroup(groupAddr, ni);
        multicastSocket.close();

        tcpSocket = new Socket(masterHost, masterPort);
        reader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream(),
                StandardCharsets.UTF_8));
        writer = new BufferedWriter(new OutputStreamWriter(tcpSocket.getOutputStream(),
                StandardCharsets.UTF_8));
    }

    /**
     * Reads the next message from the Master and parses it into a Message object.
     *
     * @return parsed Message instance
     * @throws IOException if reading from the TCP socket fails
     */
    public Message getMessage() throws IOException {
        String raw = reader.readLine();
        return Message.parse(raw);
    }

    /**
     * Sends an answer string to the Master over the established TCP connection.
     *
     * @param answer text to send (e.g., "Answer true" or "Answer false")
     * @throws IOException if writing to the TCP socket fails
     */
    public void sendAnswer(String answer) throws IOException {
        writer.write(answer);
        writer.newLine();
        writer.flush();
    }


    /**
     * Closes all network resources: multicast socket, TCP socket, reader, and writer.
     * <p>
     * Any IOException during close is suppressed.
     * </p>
     */
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
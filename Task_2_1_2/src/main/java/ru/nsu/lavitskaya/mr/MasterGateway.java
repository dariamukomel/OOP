package ru.nsu.lavitskaya.mr;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class MasterGateway {
    private static final String MULTICAST_GROUP = "224.0.0.1";
    private static final int MULTICAST_PORT = 5000;
    private MulticastSocket multicastSocket;
    private Socket tcpSocket;
    private BufferedReader reader;
    private BufferedWriter writer;


    public void connect() throws IOException {
        multicastSocket = new MulticastSocket(MULTICAST_PORT);
        InetAddress group = InetAddress.getByName(MULTICAST_GROUP);
        multicastSocket.joinGroup(group);

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        multicastSocket.receive(packet);

        String msg = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8).trim();
        String[] parts = msg.split(":", 2);
        if (parts.length != 2) {
            throw new IOException("Invalid master announcement: " + msg);
        }
        String masterHost = parts[0];
        int masterPort = Integer.parseInt(parts[1]);

        multicastSocket.leaveGroup(group);
        multicastSocket.close();

        tcpSocket = new Socket(masterHost, masterPort);
        reader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream(), StandardCharsets.UTF_8));
        writer = new BufferedWriter(new OutputStreamWriter(tcpSocket.getOutputStream(), StandardCharsets.UTF_8));
    }

    public String getMessage() throws IOException {
        return reader.readLine();
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

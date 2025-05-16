package ru.nsu.lavitskaya.mr;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WorkersGateway {
    private static final String MULTICAST_GROUP = "224.0.0.1";
    private static final int MULTICAST_PORT = 5000;
    private static final int SERVER_PORT = 6000;

    private ServerSocket serverSocket;
    private final ConcurrentMap<String, Socket> workerSockets = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, BufferedReader> readers = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, BufferedWriter> writers = new ConcurrentHashMap<>();

    public List<String> discover() throws IOException {
        serverSocket = new ServerSocket(SERVER_PORT);
        serverSocket.setReuseAddress(true);

        String localHost = InetAddress.getLocalHost().getHostAddress();
        String announcement = localHost + ":" + SERVER_PORT;
        byte[] buf = announcement.getBytes(StandardCharsets.UTF_8);
        try (DatagramSocket ds = new DatagramSocket()) {
            DatagramPacket packet = new DatagramPacket(
                    buf, buf.length,
                    InetAddress.getByName(MULTICAST_GROUP),
                    MULTICAST_PORT
            );
            ds.send(packet);
        }

        long timeoutMillis = 5000;
        long endTime = System.currentTimeMillis() + timeoutMillis;
        List<String> connectedWorkers = new ArrayList<>();

        while (true) {
            long remaining = endTime - System.currentTimeMillis();
            if (remaining <= 0) {
                break;
            }
            serverSocket.setSoTimeout((int) remaining);
            try {
                Socket workerSocket = serverSocket.accept();
                String workerId = workerSocket.getRemoteSocketAddress().toString();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(workerSocket.getInputStream(), StandardCharsets.UTF_8)
                );
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(workerSocket.getOutputStream(), StandardCharsets.UTF_8)
                );

                workerSockets.put(workerId, workerSocket);
                readers.put(workerId, reader);
                writers.put(workerId, writer);
                connectedWorkers.add(workerId);
            } catch (SocketTimeoutException e) {
                break;
            }
        }

        if (connectedWorkers.isEmpty()) {
            serverSocket.close();
            throw new IOException("No workers connected within 5-second timeout");
        }
        return connectedWorkers;
    }

    public void sendTasks(String workerId, int[] numbers) throws IOException {
        BufferedWriter writer = writers.get(workerId);
        if (writer == null) {
            throw new IOException("Worker not connected: " + workerId);
        }
        StringBuilder sb = new StringBuilder("Task ");
        for (int i = 0; i < numbers.length; i++) {
            sb.append(numbers[i]);
            if (i < numbers.length - 1) sb.append(",");
        }
        writer.write(sb.toString());
        writer.newLine();
        writer.flush();
    }

    public String getAnswer(String workerId) throws IOException {
        BufferedReader reader = readers.get(workerId);
        if (reader == null) {
            throw new IOException("Worker not connected: " + workerId);
        }
        return reader.readLine();
    }

    public void sendCommand(String workerId, String command) throws IOException {
        BufferedWriter writer = writers.get(workerId);
        if (writer == null) {
            throw new IOException("Worker not connected: " + workerId);
        }
        writer.write(command);
        writer.newLine();
        writer.flush();
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException ignored) {
        }
        workerSockets.values().forEach(s -> {
            try {
                s.close();
            } catch (IOException ignored) {
            }
        });
    }
}

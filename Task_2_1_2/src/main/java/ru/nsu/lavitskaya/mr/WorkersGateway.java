package ru.nsu.lavitskaya.mr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Manages communication with multiple Worker instances.
 * <p>
 * Sends a multicast announcement to invite Workers to connect,
 * accepts incoming TCP connections, and allows sending tasks and commands
 * to individual Workers as well as receiving their responses.
 * </p>
 */
public class WorkersGateway {
    private static final String MULTICAST_GROUP = "230.0.0.1";
    private static final int MULTICAST_PORT = 4446;
    private static final int SERVER_PORT = 6000;

    private final boolean useLoopback;
    private ServerSocket serverSocket;
    private final ConcurrentMap<String, Socket> workerSockets = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, BufferedReader> readers = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, BufferedWriter> writers = new ConcurrentHashMap<>();

    /**
     * Constructs a WorkersGateway.
     *
     * @param useLoopback if true, use the loopback interface for network operations;
     *                    otherwise use the default host interface
     */
    public WorkersGateway(boolean useLoopback) {
        this.useLoopback = useLoopback;
    }

    /**
     * Discovers Worker instances by sending a multicast announcement
     * and accepting TCP connections within a timeout.
     * <p>
     * Each Worker that receives the announcement should connect back via TCP.
     * The method returns identifiers of all connected Worker sockets.
     * </p>
     *
     * @return list of connected Worker identifiers (remote socket addresses)
     * @throws IOException if network interfaces cannot be found, if no
     *                     Workers connect within the timeout, or other I/O errors
     */
    public List<String> discover() throws IOException {
        serverSocket = new ServerSocket(SERVER_PORT);
        serverSocket.setReuseAddress(true);

        InetAddress addr = useLoopback
                ? InetAddress.getLoopbackAddress()
                : InetAddress.getLocalHost();

        String announcement = addr.getHostAddress() + ":" + SERVER_PORT;
        byte[] buf = announcement.getBytes(StandardCharsets.UTF_8);

        InetAddress group = InetAddress.getByName(MULTICAST_GROUP);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, MULTICAST_PORT);

        NetworkInterface ni = NetworkInterface.getByInetAddress(addr);
        if (ni == null) {
            throw new IOException("Не удалось найти сетевой интерфейс для адреса " + addr);
        }

        try (MulticastSocket ms = new MulticastSocket()) {
            ms.setNetworkInterface(ni);
            ms.send(packet);
        }

        long timeoutMillis = 5_000;
        long endTime = System.currentTimeMillis() + timeoutMillis;
        List<String> connectedWorkers = new ArrayList<>();

        while (System.currentTimeMillis() < endTime) {
            long remaining = endTime - System.currentTimeMillis();
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
            throw new IOException("Ни один воркер не подключился за 5 секунд");
        }
        return connectedWorkers;
    }

    /**
     * Sends a TASK message with the given numbers to the specified Worker.
     * <p>
     * The format is "Task n1,n2,..." sent over the Worker TCP connection.
     * </p>
     *
     * @param workerId identifier of the Worker (remote socket address)
     * @param numbers  array of integers to check
     * @throws IOException if the Worker ID is not connected or writing fails
     */
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

    /**
     * Reads the next line of response from the specified Worker.
     *
     * @param workerId identifier of the Worker (remote socket address)
     * @return the raw response string from the Worker
     * @throws IOException if the Worker ID is not connected or reading fails
     */
    public String getAnswer(String workerId) throws IOException {
        BufferedReader reader = readers.get(workerId);
        if (reader == null) {
            throw new IOException("Worker not connected: " + workerId);
        }
        return reader.readLine();
    }

    /**
     * Sends an arbitrary command string to the specified Worker.
     * <p>
     * Can be used for control commands like "Terminate".
     * </p>
     *
     * @param workerId identifier of the Worker (remote socket address)
     * @param command  the command string to send
     * @throws IOException if the Worker ID is not connected or writing fails
     */
    public void sendCommand(String workerId, String command) throws IOException {
        BufferedWriter writer = writers.get(workerId);
        if (writer == null) {
            throw new IOException("Worker not connected: " + workerId);
        }
        writer.write(command);
        writer.newLine();
        writer.flush();
    }

    /**
     * Closes the server socket and all connected Worker sockets.
     * <p>
     * Suppresses any I/O exceptions thrown during close operations.
     * </p>
     */
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



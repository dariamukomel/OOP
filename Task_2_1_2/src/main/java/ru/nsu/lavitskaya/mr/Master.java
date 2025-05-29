package ru.nsu.lavitskaya.mr;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Master process that distributes arrays of integers to multiple Worker instances,
 * collects their responses, and determines if any composite numbers are present.
 * <p>
 * Uses {@link WorkersGateway} for network discovery and communication,
 * and a thread pool with {@link CompletionService} for concurrent task execution.
 * </p>
 */
public class Master {
    private final WorkersGateway gateway;
    private final BlockingQueue<int[]> taskQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> freeWorkers = new LinkedBlockingQueue<>();
    private final ExecutorService executor;
    private final CompletionService<TaskResult> cs;

    /**
     * Constructs a Master instance.
     *
     * @param poolSize the number of threads to use for task execution
     * @param useLoopback if true, use the loopback interface for network operations
     */
    public Master(int poolSize, boolean useLoopback) {
        this.executor = Executors.newFixedThreadPool(poolSize);
        this.cs = new ExecutorCompletionService<>(executor);
        this.gateway = new WorkersGateway(useLoopback);
    }

    /**
     * Executes the distributed prime checking algorithm on the provided numbers.
     * <ol>
     *   <li>Discovers available workers via multicast.</li>
     *   <li>Partitions the input array into chunks equal to the number of workers.</li>
     *   <li>Submits each chunk to a worker and monitors for results.</li>
     *   <li>Upon detecting a composite, terminates early; otherwise, continues until all chunks
     *       processed.</li>
     *   <li>Sends a "Terminate" command to all workers and shuts down resources.</li>
     * </ol>
     *
     * @param numbers array of integers to check for compositeness
     * @throws IOException          if network discovery or communication fails
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public void execute(int[] numbers) throws IOException, InterruptedException {
        List<String> workers = gateway.discover();
        System.out.println("Discovered workers: " + workers);
        if (workers.isEmpty()) {
            throw new IllegalStateException("No workers available");
        }

        freeWorkers.addAll(workers);
        List<int[]> chunks = partition(numbers, workers.size());
        taskQueue.addAll(chunks);
        int totalTasks = chunks.size();
        int submitted = 0;
        int completed = 0;
        boolean compositeFound = false;

        while (submitted < totalTasks && !freeWorkers.isEmpty()) {
            String wid = freeWorkers.poll();
            int[] chunk = taskQueue.poll();
            if (chunk == null) {
                break;
            }
            submitTask(wid, chunk);
            submitted++;
        }

        while (completed < submitted && !compositeFound) {
            Future<TaskResult> future = cs.take();
            TaskResult result;
            try {
                result = future.get();
            } catch (ExecutionException ex) {
                Throwable cause = ex.getCause();
                if (cause instanceof WorkerDisconnectedException) {
                    int[] failedChunk = ((WorkerDisconnectedException) cause).getChunk();
                    taskQueue.offer(failedChunk);
                    System.err.println("Worker disconnected: "
                            + ((WorkerDisconnectedException) cause).getWorkerId());
                } else {
                    System.err.println("Task execution failed: " + cause);
                }
                completed++;
                continue;
            }
            completed++;
            System.out.printf("Worker %s: composite=%b%n", result.workerId, result.hasComposite);
            if (result.hasComposite) {
                compositeFound = true;
                break;
            }
            freeWorkers.offer(result.workerId);
            int[] next = taskQueue.poll();
            if (next != null) {
                String wid = freeWorkers.poll();
                submitTask(wid, next);
                submitted++;
            }
        }

        for (String wid : workers) {
            try {
                gateway.sendCommand(wid, "Terminate");
            } catch (IOException e) {
            }
        }

        gateway.close();
        executor.shutdownNow();

        if (compositeFound) {
            System.out.println("Found at least one composite number. Exiting.");
        } else {
            System.out.println("All numbers are prime. Exiting.");
        }
    }

    /**
     * Submits a partitioned task to a specific worker via the completion service.
     *
     * @param workerId identifier of the target worker
     * @param chunk array of integers to process
     */
    private void submitTask(String workerId, int[] chunk) {
        cs.submit(() -> {
            try {
                gateway.sendTasks(workerId, chunk);
                String line = gateway.getAnswer(workerId);
                if (line == null) {
                    throw new WorkerDisconnectedException(workerId, chunk);
                }
                String[] parts = line.split("\\s+");
                boolean hasComposite = Boolean.parseBoolean(parts[1]);
                return new TaskResult(workerId, hasComposite);
            } catch (IOException e) {
                throw new WorkerDisconnectedException(workerId, chunk, e);
            }
        });
    }

    /**
     * Partitions an array into the given number of subarrays of nearly equal size.
     *
     * @param array the input array to partition
     * @param parts number of partitions to create
     * @return list of int[] chunks summing to the original array
     */
    private static List<int[]> partition(int[] array, int parts) {
        List<int[]> result = new ArrayList<>(parts);
        int n = array.length;
        int base = n / parts;
        int rem = n % parts;
        int idx = 0;
        for (int i = 0; i < parts; i++) {
            int size = base + (i < rem ? 1 : 0);
            int[] chunk = new int[size];
            System.arraycopy(array, idx, chunk, 0, size);
            result.add(chunk);
            idx += size;
        }
        return result;
    }

    /**
     * Holds the result of a task executed by a worker.
     */
    private static class TaskResult {
        final String workerId;
        final boolean hasComposite;

        TaskResult(String workerId, boolean hasComposite) {
            this.workerId = workerId;
            this.hasComposite = hasComposite;
        }
    }

    /**
     * Exception indicating a worker disconnected or I/O failure during task execution.
     */
    private static class WorkerDisconnectedException extends RuntimeException {
        private final String workerId;
        private final int[] chunk;

        WorkerDisconnectedException(String workerId, int[] chunk) {
            super("Worker disconnected: " + workerId);
            this.workerId = workerId;
            this.chunk = chunk;
        }

        WorkerDisconnectedException(String workerId, int[] chunk, Throwable cause) {
            super("Worker I/O failure: " + workerId, cause);
            this.workerId = workerId;
            this.chunk = chunk;
        }

        public String getWorkerId() {
            return workerId;
        }

        public int[] getChunk() {
            return chunk;
        }
    }

    /**
     * Application entry point.
     * <p>
     * Reads integers from standard input and starts the execution.
     * Requires a pool size argument, and accepts optional 'test' flag to force loopback usage.
     * </p>
     *
     * @param args command-line arguments: first is thread pool size, optionally include 'test' to
     *     use loopback
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Master <poolSize> [test]");
            System.exit(1);
        }
        int poolSize;
        try {
            poolSize = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Invalid pool size: " + args[0]);
            System.exit(2);
            return;
        }
        boolean useLoopback = false;
        if (args.length > 1) {
            useLoopback = Arrays.asList(Arrays.copyOfRange(args, 1, args.length))
                    .contains("test");
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter integers separated by spaces:");
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            System.err.println("No numbers provided. Exiting.");
            System.exit(3);
        }

        int[] numbers;
        try {
            numbers = Arrays.stream(line.split("\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        } catch (NumberFormatException e) {
            System.err.println("Invalid input. Please enter only integers.");
            System.exit(4);
            return;
        }

        Master master = new Master(poolSize, useLoopback);
        try {
            master.execute(numbers);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(5);
        }
    }
}


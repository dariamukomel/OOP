package ru.nsu.lavitskaya.mr;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Master {
    private final WorkersGateway gateway = new WorkersGateway();
    private final BlockingQueue<int[]> taskQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> freeWorkers = new LinkedBlockingQueue<>();
    private final ExecutorService executor;
    private final CompletionService<TaskResult> cs;

    public Master(int poolSize) {
        this.executor = Executors.newFixedThreadPool(poolSize);
        this.cs = new ExecutorCompletionService<>(executor);
    }

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
                    System.err.println("Worker disconnected: " + ((WorkerDisconnectedException) cause).getWorkerId());
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
            } catch (IOException e) {}
        }

        gateway.close();
        executor.shutdownNow();

        if (compositeFound) {
            System.out.println("Found at least one composite number. Exiting.");
        } else {
            System.out.println("All numbers are prime. Exiting.");
        }
    }

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

    private static class TaskResult {
        final String workerId;
        final boolean hasComposite;

        TaskResult(String workerId, boolean hasComposite) {
            this.workerId = workerId;
            this.hasComposite = hasComposite;
        }
    }

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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter integers separated by spaces:");
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            System.err.println("No numbers provided. Exiting.");
            System.exit(1);
        }
        int[] numbers;
        try {
            numbers = Arrays.stream(line.split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        } catch (NumberFormatException e) {
            System.err.println("Invalid input. Please enter only integers.");
            System.exit(2);
            return;
        }

        Master master = new Master(numbers.length);
        try {
            master.execute(numbers);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(3);
        }
    }
}


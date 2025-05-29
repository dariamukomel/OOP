package ru.nsu.lavitskaya.mr;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for the Master and Worker components.
 * <p>
 * These tests launch Worker and Master in separate threads,
 * execute tasks programmatically, and assert correct console output.
 * </p>
 */
public class MasterWorkerIntegrationTest {
    /**
     * Verifies that the Master identifies a composite number when provided
     * a mix of composite and prime values (e.g., 4, 7, 11).
     */
    @Test
    void testCompositeNumbers() throws Exception {
        Thread workerThread = new Thread(() -> new Worker(true).start());
        workerThread.setDaemon(true);
        workerThread.start();
        TimeUnit.SECONDS.sleep(1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        PrintStream oldOut = System.out;
        System.setOut(ps);
        try {
            Master master = new Master(1, true);
            master.execute(new int[]{4, 7, 11});
        } finally {
            System.setOut(oldOut);
        }

        String output = out.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Found at least one composite"));
    }

    /**
     * Verifies that the Master confirms all numbers are prime when given
     * only prime values (e.g., 2, 3, 5).
     */
    @Test
    void testAllPrimeNumbers() throws Exception {
        Thread workerThread = new Thread(() -> new Worker(true).start());
        workerThread.setDaemon(true);
        workerThread.start();
        TimeUnit.SECONDS.sleep(1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        PrintStream oldOut = System.out;
        System.setOut(ps);
        try {
            Master master = new Master(1, true);
            master.execute(new int[]{2, 3, 5});
        } finally {
            System.setOut(oldOut);
        }

        String output = out.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("All numbers are prime"));
    }

    /**
     * Verifies that the Master confirms all numbers are prime when using two
     * Workers and a large array of the first 200 primes loaded
     * from the resource file "primes.txt".
     */
    @Test
    void testTwoWorkersAllPrimesLargeArray() throws Exception {
        // Запускаем два воркера
        Thread w1 = new Thread(() -> new Worker(true).start());
        Thread w2 = new Thread(() -> new Worker(true).start());
        w1.setDaemon(true);
        w2.setDaemon(true);
        w1.start();
        w2.start();
        TimeUnit.SECONDS.sleep(1);

        InputStream is = getClass().getResourceAsStream("/primes.txt");
        List<String> primes;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {
            primes = reader.lines()
                    .flatMap(line -> List.of(line.trim().split("[ ,]+?"))
                            .stream())
                    .filter(token -> !token.isEmpty())
                    .collect(Collectors.toList());
        }
        int[] numbers = primes.stream().mapToInt(Integer::parseInt).toArray();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        PrintStream oldOut = System.out;
        System.setOut(ps);
        try {
            Master master = new Master(2, true);
            master.execute(numbers);
        } finally {
            System.setOut(oldOut);
        }

        String output = out.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("All numbers are prime"));
    }
}

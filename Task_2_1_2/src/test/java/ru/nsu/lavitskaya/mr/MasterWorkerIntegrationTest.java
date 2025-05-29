package ru.nsu.lavitskaya.mr;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for the Master and Worker components.
 * <p>
 * These tests launch worker and master processes, send tasks via standard input,
 * and assert correct responses on standard output.
 * </p>
 */
public class MasterWorkerIntegrationTest {
    private static final String CLASS_PATH = System.getProperty("java.class.path");

    /**
     * Verifies that the Master identifies a composite number when provided
     * a mix of composite and prime values (e.g., 4, 7, 11).
     *
     * @throws Exception if process launch or I/O operations fail
     */
    @Test
    void testCompositeNumbers() throws Exception {
        ProcessBuilder workerPb = new ProcessBuilder(
                "java", "-cp", CLASS_PATH, "ru.nsu.lavitskaya.mr.Worker"
        ).redirectErrorStream(true);
        Process worker = workerPb.start();
        Thread.sleep(1000);

        ProcessBuilder masterPb = new ProcessBuilder(
                "java", "-cp", CLASS_PATH, "ru.nsu.lavitskaya.mr.Master"
        );
        Process master = masterPb.start();

        try (BufferedWriter masterIn = new BufferedWriter(
                new OutputStreamWriter(master.getOutputStream())
        )) {
            masterIn.write("4 7 11");
            masterIn.newLine();
            masterIn.flush();
        }

        BufferedReader masterO = new BufferedReader(new InputStreamReader(master.getInputStream()));
        String line;
        boolean compositeDetected = false;
        while ((line = masterO.readLine()) != null) {
            if (line.contains("Found at least one composite")) {
                compositeDetected = true;
                break;
            }
        }
        assertTrue(compositeDetected);

        master.waitFor(5, TimeUnit.SECONDS);
        worker.destroyForcibly();
    }

    /**
     * Verifies that the Master confirms all numbers are prime when given
     * only prime values (e.g., 2, 3, 5).
     *
     * @throws Exception if process launch or I/O operations fail
     */
    @Test
    void testAllPrimeNumbers() throws Exception {
        ProcessBuilder workerPb = new ProcessBuilder(
                "java", "-cp", CLASS_PATH, "ru.nsu.lavitskaya.mr.Worker"
        ).redirectErrorStream(true);
        Process worker = workerPb.start();
        Thread.sleep(1000);

        ProcessBuilder masterPb = new ProcessBuilder(
                "java", "-cp", CLASS_PATH, "ru.nsu.lavitskaya.mr.Master"
        );
        Process master = masterPb.start();

        try (BufferedWriter masterIn = new BufferedWriter(
                new OutputStreamWriter(master.getOutputStream())
        )) {
            masterIn.write("2 3 5");
            masterIn.newLine();
            masterIn.flush();
        }

        BufferedReader masterO = new BufferedReader(new InputStreamReader(master.getInputStream()));
        String line;
        boolean allPrime = false;
        while ((line = masterO.readLine()) != null) {
            if (line.contains("All numbers are prime")) {
                allPrime = true;
                break;
            }
        }
        assertTrue(allPrime);

        master.waitFor(5, TimeUnit.SECONDS);
        worker.destroyForcibly();
    }

    /**
     * Verifies that the Master confirms all numbers are prime when using two
     * Workers and a large array of the first 200 primes loaded
     * from the resource file "primes.txt".
     *
     * @throws Exception if process launch or I/O operations fail
     */
    @Test
    void testTwoWorkersAllPrimesLargeArray() throws Exception {
        ProcessBuilder workerPb1 = new ProcessBuilder(
                "java", "-cp", CLASS_PATH, "ru.nsu.lavitskaya.mr.Worker"
        ).redirectErrorStream(true);
        Process worker1 = workerPb1.start();

        ProcessBuilder workerPb2 = new ProcessBuilder(
                "java", "-cp", CLASS_PATH, "ru.nsu.lavitskaya.mr.Worker"
        ).redirectErrorStream(true);
        Process worker2 = workerPb2.start();

        Thread.sleep(1000);

        ProcessBuilder masterPb = new ProcessBuilder(
                "java", "-cp", CLASS_PATH, "ru.nsu.lavitskaya.mr.Master"
        );
        Process master = masterPb.start();

        List<String> primes;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/primes.txt"), StandardCharsets.UTF_8))) {
            primes = reader.lines()
                    .flatMap(line -> List.of(line.trim().split("[ ,]+?")).stream())
                    .filter(token -> !token.isEmpty())
                    .collect(Collectors.toList());
        }
        String payload = String.join(" ", primes);
        try (BufferedWriter masterIn = new BufferedWriter(
                new OutputStreamWriter(master.getOutputStream(), StandardCharsets.UTF_8))) {
            masterIn.write(payload);
            masterIn.newLine();
            masterIn.flush();
        }

        InputStreamReader inputStreamReader = new InputStreamReader(master.getInputStream(),
                StandardCharsets.UTF_8);
        BufferedReader masterO = new BufferedReader(inputStreamReader);
        String line;
        boolean allPrimeDetected = false;
        while ((line = masterO.readLine()) != null) {
            if (line.contains("All numbers are prime")) {
                allPrimeDetected = true;
                break;
            }
        }
        assertTrue(allPrimeDetected);

        master.waitFor(5, TimeUnit.SECONDS);
        worker1.destroyForcibly();
        worker2.destroyForcibly();
    }
}

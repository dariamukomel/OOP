package ru.nsu.lavitskaya.substring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * The {@code SearchSubstringTest} class contains tests to verify the functionality
 * of substring searching in various contexts using JUnit 5.
 * The tests include searching for a substring in a large file, in resources,
 * and checking the handling of an empty substring. Each test uses different approaches
 * to create test data and handle it.
 */
class SearchSubstringTest {
    private Path tempFile;

    @AfterEach
    public void cleanup() throws IOException {
        if (tempFile != null && Files.exists(tempFile)) {
            Files.delete(tempFile);
        }
    }

    @Test
    public void testFindSubstringSize8Gb() throws IOException {
        tempFile = Files.createTempFile("test_input_", ".txt");

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {
            long targetSizeInBytes = 8L * 1024 * 1024 * 1024;
            long bytesWritten = 0;
            String content = "абракадабра";
            while (bytesWritten < targetSizeInBytes) {
                if (bytesWritten % (1024 * 1024) == 0) {
                    writer.write(content);
                    bytesWritten += content.getBytes().length;
                } else {
                    writer.write(".".repeat(1024 * 1024 - content.getBytes().length));
                    bytesWritten += 1024 * 1024 - content.getBytes().length;
                }
            }
        }
        String substring = "абракадабра";
        List<Long> occurrences = SearchSubstring.find(tempFile.toString(), substring);

        assertEquals(8192, occurrences.size());
        long index = 0;
        for (int i = 0; i < occurrences.size(); i++) {
            assertEquals(index, occurrences.get(i));
            index += 1024 * 1024 - substring.length();
        }
    }

    @Test
    public void testFindSubstringInResource() throws IOException {
        String resourcePath = "/test_1.txt";
        String substring = "hello";
        List<Long> occurrences = SearchSubstring.find(resourcePath, substring);

        assertEquals(2, occurrences.size());
        assertEquals(4, occurrences.get(0));
        assertEquals(19, occurrences.get(1));
    }

    @Test
    public void testFindSubstringInResourceWithEmptySubstring() throws IOException {
        String resourcePath = "/test_1.txt";
        String substring = "";

        List<Long> occurrences = SearchSubstring.find(resourcePath, substring);

        assertEquals(0, occurrences.size());
    }


}
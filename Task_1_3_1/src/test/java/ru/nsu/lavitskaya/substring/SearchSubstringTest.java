package ru.nsu.lavitskaya.substring;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchSubstringTest {
    private Path tempFile;

    @AfterEach
    public void cleanup() throws IOException {
        if (tempFile != null && Files.exists(tempFile)) {
            Files.delete(tempFile);
        }
    }

    @Test
    public void testFindFileSize1MB() throws IOException {
        String content = "абракадабра";
        int contentsBytesLength =  content.getBytes().length;
        long targetSizeInBytes = (long) 1024 * 1024 ;
        long bytesWritten = 0;

        tempFile = Files.createTempFile("test_input_", ".txt");

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {
            while (bytesWritten + contentsBytesLength < targetSizeInBytes) {
                writer.write(content);
                bytesWritten += content.getBytes(StandardCharsets.UTF_8).length;
            }
        }

        String substring = "абракадабра";
        List<Integer> occurrences = SearchSubstring.find(tempFile.toString(), substring);

        assertEquals(occurrences.size(), targetSizeInBytes/ contentsBytesLength);
        assertEquals(occurrences.get(0), 0);
        assertEquals(occurrences.get(1), content.length());
        assertEquals(occurrences.getLast(), content.length()*(occurrences.size()-1));

    }



}
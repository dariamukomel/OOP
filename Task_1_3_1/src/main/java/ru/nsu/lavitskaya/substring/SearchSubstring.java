package ru.nsu.lavitskaya.substring;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code SearchSubstring} class provides methods to find all occurrences
 * of a specified substring within a given text resource or file.
 * It can read from both regular files and resources bundled within a JAR file.
 * The positions of all occurrences of the substring in the file are returned
 * as a list of indices.
 */
public class SearchSubstring {

    /**
     * Finds all occurrences of the specified substring within the file or resource
     * located at the given path.
     *
     * @param path      the path to the file or resource from which to read the text.
     * @param substring the substring to search for within the text. An empty substring
     *                  will result in an empty list being returned.
     * @return a list of indices indicating the starting positions of each occurrence
     *         of the substring in the text. If the substring is empty, returns
     *         an empty list.
     * @throws IOException if an I/O error occurs while reading the file or resource.
     */
    public static List<Long> find(String path, String substring) throws IOException {
        List<Long> indices = new ArrayList<>();
        int substringLength = substring.length();

        if (substringLength == 0) {
            return indices;
        }

        InputStream inputStream = SearchSubstring.class.getResourceAsStream(path);

        if (inputStream == null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(path,
                    StandardCharsets.UTF_8))) {
                indices = readFromReader(reader, substring, indices);
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
                    StandardCharsets.UTF_8))) {
                indices = readFromReader(reader, substring, indices);
            }
        }

        return indices;
    }

    /**
     * Reads characters from the provided {@code BufferedReader} and checks for
     * occurrences of the specified substring. The method maintains a buffer of
     * characters to facilitate substring matching.
     *
     * @param reader     the {@code BufferedReader} from which to read characters.
     * @param substring  the substring to search for within the characters being read.
     * @param indices    the list to which the starting indices of found occurrences
     *                   will be added.
     * @return the list of indices of occurrences of the substring in the text.
     * @throws IOException if an I/O error occurs while reading characters.
     */
    private static List<Long> readFromReader(BufferedReader reader, String substring,
                                             List<Long> indices) throws IOException {
        StringBuilder currentBuffer = new StringBuilder();
        long currentIndex = 0;
        int substringLength = substring.length();

        int character;
        while ((character = reader.read()) != -1) {
            currentBuffer.append((char) character);
            currentIndex++;

            if (currentBuffer.length() > substringLength) {
                currentBuffer.deleteCharAt(0);
            }

            if (currentBuffer.length() == substringLength
                    && currentBuffer.toString().equals(substring)) {
                indices.add(currentIndex - substringLength);
            }
        }
        return indices;
    }

}

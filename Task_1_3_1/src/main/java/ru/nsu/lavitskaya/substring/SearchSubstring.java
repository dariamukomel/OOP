package ru.nsu.lavitskaya.substring;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SearchSubstring {

    public static List<Integer> find(String filename, String substring) {

        List<Integer> indices = new ArrayList<>();
        int currentIndex = 0;
        int substringLength = substring.length();

        if (substringLength == 0) {
            return indices;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            StringBuilder currentBuffer = new StringBuilder();
            int substringIndex;

            while ((substringIndex = reader.read()) != -1) {
                currentBuffer.append((char) substringIndex);
                currentIndex++;

                if (currentBuffer.length() > substringLength) {
                    currentBuffer.deleteCharAt(0);
                }

                if (currentBuffer.length() == substringLength && currentBuffer.toString().equals(substring)) {
                    indices.add(currentIndex - substringLength);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return indices;
    }

}

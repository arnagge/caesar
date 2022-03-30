package com.javarush.caesarcipher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileManager {

    public static String readFileContent(String inputFilePath) {
        try {
            String content = Files.readString(Path.of(inputFilePath), StandardCharsets.UTF_8);
            if (content == null || content.length() == 0) {
                System.out.printf("Content is empty! please prefill the file: %s\n", inputFilePath);
                return null;
            }
            return content;
        } catch (IOException e) {
            System.out.printf("Couldn't read file: %s", inputFilePath);
        }
        return null;
    }

    public static void writeFileContent(String outputFilePath, String content) {
        try {
            Files.writeString(Path.of(outputFilePath), content, StandardOpenOption.WRITE);
        } catch (IOException e) {
            System.out.printf("Couldn't write file: %s", outputFilePath);
        }
    }
}

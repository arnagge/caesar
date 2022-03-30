package com.javarush.caesarcipher;

import java.util.*;

public class Solution {

    private static final List<Character> alphabetCyrillic = Arrays.asList('а', 'б', 'в',
            'г', 'д', 'е', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у',
            'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'я');

    private static final List<Character> alphabetLatin = Arrays.asList('a', 'b', 'c', 'd', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');

    private static final List<Character> numbers = Arrays.asList('0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9');

    private static final String inputFile = "caesarcipher/src/com/javarush/caesarcipher/resources/input.txt";
    private static final String encryptedFile = "caesarcipher/src/com/javarush/caesarcipher/resources/encrypted.txt";
    private static final String decryptedFile = "caesarcipher/src/com/javarush/caesarcipher/resources/decrypted.txt";

    private static final HashMap<Integer, String> modes = new HashMap<>() {{
        put(1, "шифровка текста");
        put(2, "расшифровка с помощью ключа");
        put(3, "расшифровка с помощью brute force");
        put(4, "Exit");
    }};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        CaesarCipher cipher = new CaesarCipher(alphabetCyrillic, alphabetLatin, numbers);
        selectMode(cipher, scanner);
    }

    private static void selectMode(CipherInterface cipher, Scanner scanner) {
        int selectedMode = 0;

        while (selectedMode == 0) {
            System.out.println("Please select mode:");
            for (Map.Entry<Integer, String> mode : modes.entrySet()) {
                System.out.printf("%d - %s\n", mode.getKey(), mode.getValue());
            }
            if (scanner.hasNextInt()) {
                int pickedMode = scanner.nextInt();
                if (modes.containsKey(pickedMode)) {
                    selectedMode = pickedMode;
                } else {
                    System.out.println("Wrong mode! Please select a correct one!");
                }
            }
        }

        switch (selectedMode) {
            case 1 -> {
                encryptText(cipher, scanner);
            }
            case 2 -> {
                decryptText(cipher, scanner);
            }
            case 3 -> {
                bruteForceDecryptText(cipher, scanner);
            }
            case 4 -> {
                System.exit(0);
            }
        }
    }

    private static void encryptText(CipherInterface cipher, Scanner scanner) {
        System.out.println("Choose integer key:");
        int shiftKey = scanner.nextInt();
        String content = FileManager.readFileContent(inputFile);

        if (content != null) {
            content = cipher.encrypt(content, shiftKey);
            FileManager.writeFileContent(encryptedFile, content);
            System.out.printf("Encrypted string:\n %s\n", content);
        }
        selectMode(cipher, scanner);
    }

    private static void decryptText(CipherInterface cipher, Scanner scanner) {
        System.out.println("Choose integer key:");
        int shiftKey = scanner.nextInt();
        String content = FileManager.readFileContent(encryptedFile);

        if (content != null) {
            content = cipher.decrypt(content, shiftKey);
            if (content == null || content.length() == 0) {
                System.out.println("Couldn't decrypt defined text");
                System.exit(0);
            }
            System.out.printf("Decrypted string:\n %s\n", content);
            FileManager.writeFileContent(decryptedFile, content);
        }
        selectMode(cipher, scanner);
    }

    private static void bruteForceDecryptText(CipherInterface cipher, Scanner scanner) {
        String content = FileManager.readFileContent(encryptedFile);
        if (content != null) {
            content = cipher.bruteForceDecrypt(content);
            if (content == null || content.length() == 0) {
                System.out.println("Couldn't decrypt defined text");
            }
            System.out.printf("Decrypted string:\n %s\n", content);
            FileManager.writeFileContent(decryptedFile, content);
        }
        selectMode(cipher, scanner);
    }
}
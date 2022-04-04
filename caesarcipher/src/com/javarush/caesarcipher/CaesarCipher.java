package com.javarush.caesarcipher;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class CaesarCipher implements CipherInterface {
    private List<Character> characterList = Arrays.asList('.', ',', '«', '»',
            ':', '!', '?', ' ', '\n', '"');

    private static final HashMap<Character, String> CHAR_RULES = new HashMap<>() {{
        put(',', ", ");
        put('.', ". ");
        put('«', " «");
        put('»', "» ");
        put('?', "? ");
        put('!', "! ");
        put(':', ": ");
    }};

    @SafeVarargs
    CaesarCipher(List<Character>... alphabet) {
        for (List<Character> singleAlphabet : alphabet) {
            this.characterList = Stream.of(this.characterList, singleAlphabet).filter(Objects::nonNull)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }
    }

    public String encrypt(String str, int keyCode) {
        keyCode = this.normalizeShiftKey(keyCode);
        str = str.toLowerCase(Locale.ROOT);

        int lastIdx = characterList.size() - 1;
        char[] inputChArray = str.toCharArray();

        StringBuilder stringBuilder = new StringBuilder();
        for (char ch : inputChArray) {
            int idx = characterList.indexOf(ch);

            if (idx != -1) {
                int toIdx = idx + keyCode;
                toIdx = toIdx > lastIdx ? toIdx - lastIdx - 1 : toIdx;

                stringBuilder.append(characterList.get(toIdx));
            } else {
                stringBuilder.append(ch);
            }
        }

        return stringBuilder.toString();
    }

    public String decrypt(String str, int keyCode) {
        keyCode = this.normalizeShiftKey(keyCode);
        int lastIdx = characterList.size() - 1;
        char[] inputChArray = str.toCharArray();

        StringBuilder stringBuilder = new StringBuilder();
        for (char ch : inputChArray) {
            int idx = characterList.indexOf(ch);

            if (idx != -1) {
                int toIdx = idx - keyCode;
                toIdx = toIdx < 0 ? lastIdx + 1 + toIdx : toIdx;

                stringBuilder.append(characterList.get(toIdx));
            } else {
                stringBuilder.append(ch);
            }
        }
        return stringBuilder.toString();
    }

    public String bruteForceDecrypt(String encryptedString) {
        List <String> decryptedStrings = new ArrayList<>();
        for (int i=1; i < characterList.size(); i++) {
            decryptedStrings.add(this.decrypt(encryptedString, i));
        }

        ListIterator<String> iterator = decryptedStrings.listIterator();
        while (iterator.hasNext()){
            String decryptedStr = iterator.next();
            int spacesFound = StringUtils.countMatches(decryptedStr, " ") +
                    StringUtils.countMatches(decryptedStr, "\n");
            int wordsCount = countWords(decryptedStr);

            if (wordsCount > spacesFound) {
                iterator.remove();
                continue;
            }

            for (Map.Entry<Character, String> charRule : CHAR_RULES.entrySet()) {
                int foundSpecialChars = StringUtils.countMatches(decryptedStr, charRule.getKey());
                if (foundSpecialChars == 0) {
                    continue;
                }
                if(decryptedStr.endsWith(charRule.getKey().toString())) {
                    continue;
                }

                String[] charRuleValue = {
                        charRule.getValue(),
                        charRule.getValue().replace(' ', '\n'),
                        charRule.getValue().replace(" ", "\r\n")
                };

                int foundSpecialCharRules = 0;
                for (String needleStr: charRuleValue) {
                    foundSpecialCharRules += StringUtils.countMatches(decryptedStr, needleStr);
                }

                if (foundSpecialChars > foundSpecialCharRules) {
                    iterator.remove();
                    break;
                }
            }
        }
        return decryptedStrings.size() > 0 ? decryptedStrings.get(0) : "";
    }

    private int normalizeShiftKey(int shiftKey) {
        if (shiftKey < 0) {
            shiftKey = Math.abs(shiftKey);
            shiftKey = shiftKey > characterList.size() ? shiftKey % characterList.size() : shiftKey;
            return characterList.size() - shiftKey;
        }
        if (shiftKey > characterList.size()) {
            return shiftKey % characterList.size();
        }
        return shiftKey;
    }

    public static int countWords(String sentence) {
        if (sentence == null || sentence.isEmpty()) {
            return 0;
        }
        StringTokenizer tokens = new StringTokenizer(sentence);
        return tokens.countTokens();
    }
}

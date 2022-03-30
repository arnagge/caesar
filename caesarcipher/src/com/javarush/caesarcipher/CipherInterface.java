package com.javarush.caesarcipher;

public interface CipherInterface {
    String encrypt(String str, int keyCode);
    String decrypt(String str, int keyCode);
    String bruteForceDecrypt(String str);
}

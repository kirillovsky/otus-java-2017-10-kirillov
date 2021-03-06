package ru.otus.kirillov.utils;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

/**
 * Created by Александр on 26.02.2016.
 */

public class AESSecurity {

    private final String encryptionKey;

    public AESSecurity(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String encrypt(String str) {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
        byte[] encryptedBytes;
        try {
            encryptedBytes = cipher.doFinal(str.getBytes());
        } catch (IllegalBlockSizeException|BadPaddingException e) {
            throw new RuntimeException(e);
        }

        return Base64.encode(encryptedBytes);
    }

    private Cipher getCipher(int cipherMode) {
        String encryptionAlgorithm = "AES";
        Cipher cipher;
        try {
            SecretKeySpec keySpecification = new SecretKeySpec(encryptionKey.getBytes("UTF-8"),
                    encryptionAlgorithm);
            cipher = Cipher.getInstance(encryptionAlgorithm);
            cipher.init(cipherMode, keySpecification);
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return cipher;
    }
}

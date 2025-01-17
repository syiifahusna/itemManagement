package com.itemManagement.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptDecryptUtil {

    private String ALGORITHM;
    private String SECRET_KEY;

    // Injecting values using @Value
    @Value("${spring.secret.algorithm}")
    public void setALGORITHM(String ALGORITHM) {
        this.ALGORITHM = ALGORITHM;
    }

    @Value("${spring.secret.key}")
    public void setSecretKey(String secretKey) {
        this.SECRET_KEY = secretKey;
    }

    private SecretKey getSecretKey(){
        byte[] keyBytes = SECRET_KEY.getBytes();
        SecretKey secretKey = new SecretKeySpec(keyBytes, 0, 16, ALGORITHM);
        return  secretKey;
    }


    // Encrypt the input string
    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt the encrypted string
    public String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    public String encryptInteger(int value) throws Exception {
        return encrypt(Integer.toString(value));
    }

    public int decryptInteger(String encryptedValue) throws Exception {
        return Integer.parseInt(decrypt(encryptedValue));
    }
}

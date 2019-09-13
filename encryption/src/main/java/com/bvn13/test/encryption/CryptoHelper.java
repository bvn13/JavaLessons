package com.bvn13.test.encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author bvn13
 * @since 13.09.2019
 */
public class CryptoHelper {
    private static final String FACTORY_INIT_DATA = "PBKDF2WithHmacSHA1";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";

    private static final Logger log = LoggerFactory.getLogger(CryptoHelper.class);

    public static String encrypt(String word, String password) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);

        // Derive the key

        SecretKeyFactory factory = SecretKeyFactory.getInstance(FACTORY_INIT_DATA);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), bytes, 65556, 256);
        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), ALGORITHM);

        //encrypting the word

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secret);

        AlgorithmParameters params = cipher.getParameters();

        byte[] ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] encryptedTextBytes = cipher.doFinal(word.getBytes(StandardCharsets.UTF_8));

        //prepend salt and vi

        byte[] buffer = new byte[bytes.length + ivBytes.length + encryptedTextBytes.length];

        System.arraycopy(bytes, 0, buffer, 0, bytes.length);
        System.arraycopy(ivBytes, 0, buffer, bytes.length, ivBytes.length);
        System.arraycopy(encryptedTextBytes, 0, buffer, bytes.length + ivBytes.length, encryptedTextBytes.length);

        return Base64.getEncoder().encodeToString(buffer);
    }

    public static String decrypt(String encryptedText, String password) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        //strip off the salt and iv

        ByteBuffer buffer = ByteBuffer.wrap(Base64.getDecoder().decode(encryptedText));

        byte[] saltBytes = new byte[20];
        buffer.get(saltBytes, 0, saltBytes.length);
        byte[] ivBytes = new byte[cipher.getBlockSize()];
        buffer.get(ivBytes, 0, ivBytes.length);
        byte[] encryptedTextBytes = new byte[buffer.capacity() - saltBytes.length - ivBytes.length];

        buffer.get(encryptedTextBytes);

        // Deriving the key

        SecretKeyFactory factory = SecretKeyFactory.getInstance(FACTORY_INIT_DATA);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 65556, 256);
        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), ALGORITHM);

        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));

        byte[] decryptedTextBytes;

        try {
            decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            log.error("Could not decrypt data: <"+encryptedText+">", e);
            throw e;
        }

        return new String(decryptedTextBytes);
    }


}

package org.lib.PacketProcessing.cipher;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.SecureRandom;
import java.util.Base64;

import static org.lib.Environment.EnvLoader.ENV_VARS;


public class CipherFactory {
    private static final int GCM_TAG_LENGTH = 128;
    private static final int GCM_IV_LENGTH = 12;
    private static final String AES_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final SecretKey AESKey = loadAESKey();

    public static Cipher getCipher(int mode, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(mode, AESKey, spec);
            return cipher;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Cipher", e);
        }
    }

    public static byte[] generateRandomIV() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    private static SecretKey loadAESKey() {
        byte[] encodedKey = ENV_VARS.get("AES_KEY").getBytes();
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, "AES");
    }

//    private static SecretKey generateAESKey() {
//        try {
//            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//            keyGen.init(AES_KEY_SIZE);
//
//            SecretKey key = keyGen.generateKey();
//            System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));
//
//            return keyGen.generateKey();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to generate AES key", e);
//        }
//    }
}
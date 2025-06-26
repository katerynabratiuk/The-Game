package org.lib.packet_processing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.lib.packet_processing.cipher.CipherFactory;
import org.lib.packet_processing.receive.Decoder;
import org.lib.packet_processing.receive.Decryptor;
import org.lib.packet_processing.send.Encoder;
import org.lib.packet_processing.send.Encryptor;

import javax.crypto.Cipher;
import java.util.Base64;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EncryptDecryptTest {
    @BeforeAll
    static void setupEnvKey() throws Exception {
        var envVarsField = Class.forName("org.lib.environment.EnvLoader").getDeclaredField("ENV_VARS");
        envVarsField.setAccessible(true);
        HashMap<String, String> envVars = (HashMap<String, String>) envVarsField.get(null);
        String key = Base64.getEncoder().encodeToString("1234567890abcdef".getBytes());   // demo 128 bit key
        envVars.put("AES_KEY", key);
    }

    @Test
    void testEncryptDecrypt() {
        var encryptor = new Encryptor();
        var decryptor = new Decryptor();
        byte[] payload = "test".getBytes();
        byte[] encrypted = encryptor.encrypt(payload);
        byte[] decrypted = decryptor.decrypt(encrypted);
        assertArrayEquals(payload, decrypted);
    }

    @Test
    void testCipherFactoryRandomIV() {
        byte[] iv1 = CipherFactory.generateRandomIV();
        byte[] iv2 = CipherFactory.generateRandomIV();
        assertEquals(12, iv1.length);
        assertEquals(12, iv2.length);
        assertFalse(java.util.Arrays.equals(iv1, iv2));
    }

    @Test
    void testCipherFactoryGetCipher() throws Exception {
        byte[] iv = CipherFactory.generateRandomIV();
        Cipher cipher = CipherFactory.getCipher(Cipher.ENCRYPT_MODE, iv);
        assertNotNull(cipher);
        assertEquals("AES/GCM/NoPadding", cipher.getAlgorithm());
    }

    @Test
    void testEncodeEncryptDecodeDecryptFlow() {
        var encoder = new Encoder();
        var decoder = new Decoder();
        var encryptor = new Encryptor();
        var decryptor = new Decryptor();
        byte[] payload = "test".getBytes();
        byte[] encrypted = encryptor.encrypt(payload);
        byte[] encoded = encoder.encode(encrypted);
        byte[] decoded = decoder.decode(encoded);
        byte[] decrypted = decryptor.decrypt(decoded);
        assertArrayEquals(payload, decrypted);
    }
}

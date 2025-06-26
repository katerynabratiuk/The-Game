package org.lib.packet_processing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.lib.packet_processing.receive.Decoder;
import org.lib.packet_processing.receive.Decryptor;
import org.lib.packet_processing.send.Encoder;
import org.lib.packet_processing.send.Encryptor;

import java.util.Base64;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class EncodeDecodeTest {
    @BeforeAll
    static void setupEnvKey() throws Exception {
        var envVarsField = Class.forName("org.lib.environment.EnvLoader").getDeclaredField("ENV_VARS");
        envVarsField.setAccessible(true);
        HashMap<String, String> envVars = (HashMap<String, String>) envVarsField.get(null);
        String key = Base64.getEncoder().encodeToString("1234567890abcdef".getBytes());   // demo 128 bit key
        envVars.put("AES_KEY", key);
    }

    @Test
    void testEncodeDecode() {
        var encoder = new Encoder();
        var decoder = new Decoder();
        byte[] payload = "test".getBytes();
        byte[] encoded = encoder.encode(payload);
        byte[] decoded = decoder.decode(encoded);
        assertArrayEquals(payload, decoded);
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

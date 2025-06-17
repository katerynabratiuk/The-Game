package org.lib.PacketProcessing.send;

import lombok.SneakyThrows;
import org.lib.DataStructures.payloads.EncryptedIVMessage;
import org.lib.PacketProcessing.cipher.CipherFactory;

import javax.crypto.Cipher;

public class Encryptor implements IEncryptor{
    @Override
    @SneakyThrows
    public byte[] encrypt(byte[] payload) {
        System.out.println("DEBUG: Encrypting...");

        byte[] iv = CipherFactory.generateRandomIV();
        System.out.println(iv);
        Cipher cipher = CipherFactory.getCipher(Cipher.ENCRYPT_MODE, iv);
        byte[] ciphertext = cipher.doFinal(payload);

        var encryptedMsg = new EncryptedIVMessage(iv, ciphertext);
        byte[] msgBytes = encryptedMsg.toBytes();

        return msgBytes;
    }
}

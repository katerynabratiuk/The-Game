package org.lib.PacketProcessing.receive;

import lombok.SneakyThrows;
import org.lib.DataStructures.payloads.EncryptedIVMessage;
import org.lib.PacketProcessing.cipher.CipherFactory;

import javax.crypto.Cipher;

public class Decryptor implements IDecryptor {
    @Override
    @SneakyThrows
    public byte[] decrypt(byte[] packet) {
        var encryptedMsg = EncryptedIVMessage.fromBytes(packet);
        Cipher cipher = CipherFactory.getCipher(Cipher.DECRYPT_MODE, encryptedMsg.getIv());
        return cipher.doFinal(encryptedMsg.getCiphertext());
    }
}

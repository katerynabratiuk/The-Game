package org.lib.packet_processing.receive;

import lombok.SneakyThrows;
import org.lib.data_structures.payloads.network.EncryptedIVMessage;
import org.lib.packet_processing.cipher.CipherFactory;

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

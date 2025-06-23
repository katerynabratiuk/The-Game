package org.lib.data_structures.payloads.network;

import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

public class EncryptedIVMessage {
    @Getter @Setter private byte[] iv;
    @Getter @Setter private byte[] ciphertext;

    public EncryptedIVMessage(byte[] iv, byte[] ciphertext) {
        this.iv = iv;
        this.ciphertext = ciphertext;
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(4 + iv.length + ciphertext.length);
        buffer.putInt(iv.length);
        buffer.put(iv);
        buffer.put(ciphertext);
        return buffer.array();
    }

    public static EncryptedIVMessage fromBytes(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int ivLength = buffer.getInt();
        byte[] iv = new byte[ivLength];
        buffer.get(iv);
        byte[] ciphertext = new byte[data.length - 4 - ivLength];
        buffer.get(ciphertext);
        return new EncryptedIVMessage(iv, ciphertext);
    }
}
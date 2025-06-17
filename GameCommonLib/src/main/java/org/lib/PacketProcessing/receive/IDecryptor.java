package org.lib.PacketProcessing.receive;

public interface IDecryptor {
    byte[] decrypt(byte[] packet);
}
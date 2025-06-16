package org.lib.PacketProcessing.receive;

public interface IDecryptor<T> {
    T decrypt(byte[] packet);
}
package org.lib.PacketProcessing.send;

public interface IEncryptor {
    byte[] encrypt(byte[] payload);
}

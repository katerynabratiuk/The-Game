package org.lib.packet_processing.send;

public interface IEncryptor {
    byte[] encrypt(byte[] payload);
}

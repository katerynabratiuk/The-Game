package org.lib.packet_processing.receive;

public interface IDecryptor {
    byte[] decrypt(byte[] packet);
}
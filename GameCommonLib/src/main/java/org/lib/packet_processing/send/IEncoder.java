package org.lib.packet_processing.send;

public interface IEncoder {
    byte[] encode(byte[] encryptedMsgBytes);
}

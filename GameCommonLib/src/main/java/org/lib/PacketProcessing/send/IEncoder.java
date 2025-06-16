package org.lib.PacketProcessing.send;

public interface IEncoder {
    byte[] encode(byte[] encryptedMsgBytes);
}

package org.lib.PacketProcessing.receive;

public interface IDecoder {
    byte[] decode(byte[] packet);
}

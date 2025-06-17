package org.lib.packet_processing.receive;

public interface IDecoder {
    byte[] decode(byte[] packet);
}

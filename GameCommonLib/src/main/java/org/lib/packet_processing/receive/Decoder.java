package org.lib.packet_processing.receive;

import lombok.SneakyThrows;
import org.lib.packet_processing.crc.CRC16;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Decoder implements IDecoder {
    private static final byte MAGIC = 0x13;

    @Override
    @SneakyThrows
    public byte[] decode(byte[] packet) {
        ByteBuffer buffer = ByteBuffer.wrap(packet).order(ByteOrder.BIG_ENDIAN);

        byte bMagic = buffer.get();
        if (bMagic != MAGIC) {
            throw new IllegalArgumentException("Invalid magic byte");
        }

        byte src = buffer.get();
        long pktId = buffer.getLong();
        int len = buffer.getInt();

        if (packet.length < 1 + 1 + 8 + 4 + len + 2) {
            throw new IllegalArgumentException("Invalid packet length");
        }

        byte[] payload = new byte[len];
        buffer.get(payload);

        short receivedCrc = buffer.getShort();

        short calculatedCrc = CRC16.calcCrc(packet, 0, packet.length - 2);
        if (receivedCrc != calculatedCrc) {
            throw new IllegalArgumentException("CRC mismatch");
        }

        return payload;
    }
}

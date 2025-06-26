package org.lib.packet_processing.send;

import org.lib.packet_processing.crc.CRC16;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

//MAGIC(1 byte)
//SRC (1 byte)
//PKT_ID (8 bytes)
//LEN (4 bytes)
//DATA (var)
//CRC16 (2 bytes)

public class Encoder implements IEncoder {
    private static final byte MAGIC = 0x13;

    @Override
    public byte[] encode(byte[] encryptedMsgBytes) {
        int dataLength = encryptedMsgBytes.length;
        int headerSize = 1 + 1 + 8 + 4;
        int totalSize = headerSize + dataLength + 2;  // + CRC short

        ByteBuffer buffer = ByteBuffer.allocate(totalSize).order(ByteOrder.BIG_ENDIAN);

        buffer.put(MAGIC);
        buffer.put((byte) 1);
        buffer.putLong(2);
        buffer.putInt(dataLength);
        buffer.put(encryptedMsgBytes);

        byte[] partial = Arrays.copyOf(buffer.array(), headerSize + dataLength);
        short crc = CRC16.calcCrc(partial, 0, partial.length);
        buffer.putShort(crc);

        return buffer.array();
    }
}

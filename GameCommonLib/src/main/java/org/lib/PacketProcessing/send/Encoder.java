package org.lib.PacketProcessing.send;

import org.lib.PacketProcessing.crc.CRC16;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

// MAGIC (1 byte)
//SRC    (1 byte)
//PKT_ID (8 bytes)
//LEN    (4 bytes)
//DATA   (variable)
//CRC16  (2 bytes)

public class Encoder implements IEncoder {
    private static final byte MAGIC = 0x13;

    @Override
    public byte[] encode(byte[] encryptedMsgBytes) {
        int dataLength = encryptedMsgBytes.length;
        int headerSize = 1 + 1 + 8 + 4; // magic + src + pkt_id + len
        int totalSize = headerSize + dataLength + 2; // + CRC16

        ByteBuffer buffer = ByteBuffer.allocate(totalSize).order(ByteOrder.BIG_ENDIAN);

        buffer.put(MAGIC);         // 1 byte
        buffer.put((byte) 1);      // src id
        buffer.putLong(2);         // packet ID
        buffer.putInt(dataLength); // payload length
        buffer.put(encryptedMsgBytes); // payload

        byte[] partial = Arrays.copyOf(buffer.array(), headerSize + dataLength);
        short crc = CRC16.calcCrc(partial, 0, partial.length);
        buffer.putShort(crc);

        return buffer.array();
    }
}

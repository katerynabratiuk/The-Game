package org.lib.packet_processing;

import org.junit.jupiter.api.Test;
import org.lib.packet_processing.crc.CRC16;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CrcTest {
    @Test
    void testCrc() {
        byte[] input = {0x01, 0x02, 0x03, 0x04};
        short expectedCrc = (short) 0x0FA1;
        short actualCrc = CRC16.calcCrc(input, 0, input.length);
        assertEquals(expectedCrc, actualCrc);
    }

    @Test
    void testCrcWithOffsetAndLength() {
        byte[] input = {0x00, 0x01, 0x02, 0x03, 0x04};
        short expectedCrc = CRC16.calcCrc(new byte[]{0x01, 0x02, 0x03}, 0, 3);
        short actualCrc = CRC16.calcCrc(input, 1, 3);
        assertEquals(expectedCrc, actualCrc);
    }
}

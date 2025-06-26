package org.lib.packet_processing.reliability;

import java.util.concurrent.atomic.AtomicInteger;

public class SequenceGenerator {
    // WIP - draft for reliability ACK mechanism
    private static final AtomicInteger sequence = new AtomicInteger(0);
    private static final int LIMIT = 0xFFFF; // max 16 bits (65535)

    public static int next() {
        return sequence.getAndIncrement() & LIMIT;
    }
}
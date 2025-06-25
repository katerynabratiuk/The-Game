package org.lib.packet_processing.reliability;

import java.util.concurrent.atomic.AtomicInteger;

public class SequenceGenerator {
    private static final AtomicInteger sequence = new AtomicInteger(0);

    public static int next() {
        return sequence.getAndIncrement() & 0xFFFF;
    }
}
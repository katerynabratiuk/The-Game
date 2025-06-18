package org.lib.packet_processing.strategies;

import java.net.SocketAddress;
import java.util.Set;

public interface ReceiverStrategy {
    Set<SocketAddress> getReceivers();
}

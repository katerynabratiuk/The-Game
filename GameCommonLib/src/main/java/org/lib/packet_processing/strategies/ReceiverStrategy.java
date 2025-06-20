package org.lib.packet_processing.strategies;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.Set;

public interface ReceiverStrategy {
    Collection<SocketAddress> getReceivers(); // still exposes just addresses
    void addReceiver(String clientUUID, SocketAddress address);
    void removeReceiver(String clientUUID);
}

package org.lib.packet_processing.send.strategies;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.List;

public interface IReceiverRegistryStrategy {
    Collection<SocketAddress> getReceivers();
    void addReceiver(String clientUUID, SocketAddress address);
    void removeReceiver(String clientUUID);
}

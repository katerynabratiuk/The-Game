package org.lib.packet_processing.send.strategies;

import org.lib.data_structures.payloads.NetworkPayload;

import java.net.SocketAddress;
import java.util.Collection;

public class BroadcastStrategy implements IReceiversResolveStrategy {
    private final IReceiverRegistryStrategy receiverStrategy;

    public BroadcastStrategy(IReceiverRegistryStrategy receiverStrategy) {
        this.receiverStrategy = receiverStrategy;
    }

    @Override
    public Collection<SocketAddress> resolveReceivers(NetworkPayload payload) {
        return receiverStrategy.getReceivers();
    }

    public synchronized boolean noReceivers() {
        return receiverStrategy.getReceivers().isEmpty();
    }

    public void removeReceiver(String clientUUID) {
        receiverStrategy.removeReceiver(clientUUID);
    }
}
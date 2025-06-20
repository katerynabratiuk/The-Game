package org.lib.packet_processing.strategies;

import org.lib.packet_processing.registry.SocketAddressRegistry;

import java.net.SocketAddress;
import java.util.Collection;

public class DynamicRegistryStrategy implements ReceiverStrategy {
    private final SocketAddressRegistry registry;

    public DynamicRegistryStrategy(SocketAddressRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Collection<SocketAddress> getReceivers() {
        return registry.getAll();
    }

    @Override
    public void addReceiver(String clientUUID, SocketAddress address) {
        registry.add(clientUUID, address);
    }

    @Override
    public void removeReceiver(String clientUUID) {
        registry.remove(clientUUID);
    }
}

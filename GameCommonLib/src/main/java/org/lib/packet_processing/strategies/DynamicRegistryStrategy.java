package org.lib.packet_processing.strategies;

import org.lib.packet_processing.registry.SocketAddressRegistry;

import java.net.SocketAddress;
import java.util.Set;

public class DynamicRegistryStrategy implements ReceiverStrategy {
    private final SocketAddressRegistry registry;

    public DynamicRegistryStrategy(SocketAddressRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Set<SocketAddress> getReceivers() {
        return registry.getAll();
    }
}

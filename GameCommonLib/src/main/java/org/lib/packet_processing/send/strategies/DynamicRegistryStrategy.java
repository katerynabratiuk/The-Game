package org.lib.packet_processing.send.strategies;

import lombok.AllArgsConstructor;
import org.lib.packet_processing.registry.SocketAddressRegistry;

import java.net.SocketAddress;
import java.util.Collection;

@AllArgsConstructor
public class DynamicRegistryStrategy implements IReceiverRegistryStrategy {
    private final SocketAddressRegistry registry;

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

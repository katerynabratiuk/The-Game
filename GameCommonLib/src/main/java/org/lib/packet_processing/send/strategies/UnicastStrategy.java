package org.lib.packet_processing.send.strategies;

import org.lib.data.payloads.NetworkPayload;
import org.lib.packet_processing.registry.SocketAddressRegistry;

import java.net.SocketAddress;
import java.util.List;

public class UnicastStrategy implements IReceiversResolveStrategy {
    private final SocketAddressRegistry registry;

    public UnicastStrategy(SocketAddressRegistry registry) {
        this.registry = registry;
    }

    @Override
    public List<SocketAddress> resolveReceivers(NetworkPayload payload) {
        return List.of(registry.get(payload.getClientUUID()));
    }
}

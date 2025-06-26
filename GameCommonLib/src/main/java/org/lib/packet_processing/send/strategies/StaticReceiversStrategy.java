package org.lib.packet_processing.send.strategies;

import lombok.AllArgsConstructor;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.Map;

@AllArgsConstructor
public class StaticReceiversStrategy implements IReceiverRegistryStrategy {
    private Map<String, SocketAddress> receivers;

    @Override
    public Collection<SocketAddress> getReceivers() {
        return receivers.values();
    }

    @Override
    public void addReceiver(String clientUUID, SocketAddress address) {
        receivers.put(clientUUID, address);
    }

    @Override
    public void removeReceiver(String clientUUID) {
        receivers.remove(clientUUID);
    }
}
package org.lib.packet_processing.strategies;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StaticReceiversStrategy implements ReceiverStrategy {
    private Map<String, SocketAddress> receivers = new ConcurrentHashMap<>();

    public StaticReceiversStrategy(Map<String, SocketAddress> receivers) {
        this.receivers = receivers;
    }

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
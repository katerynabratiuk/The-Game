package org.lib.packet_processing.registry;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SocketAddressRegistry implements ISocketAddressRegistry{
    private final Map<String, SocketAddress> receivers = new ConcurrentHashMap<>();

    @Override
    public SocketAddress get(String clientUUID) {
        return receivers.get(clientUUID);
    }

    @Override
    public Collection<SocketAddress> getAll() {
        return receivers.values();
    }

    @Override
    public void add(String key, SocketAddress address) {
        receivers.put(key, address);
    }

    @Override
    public void remove(String key) {
        receivers.remove(key);
    }

    public Optional<String> findReceiverByAddress(SocketAddress address) {
        return receivers.entrySet().stream()
                .filter(entry -> entry.getValue().equals(address))
                .map(Map.Entry::getKey)
                .findFirst();
    }
}

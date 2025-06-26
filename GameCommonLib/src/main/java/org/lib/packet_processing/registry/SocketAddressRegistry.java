package org.lib.packet_processing.registry;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SocketAddressRegistry implements ISocketAddressRegistry{
    private final Map<String, SocketAddress> receivers = new ConcurrentHashMap<>();
    private final List<IReceiverRegistryObserver> observers = new CopyOnWriteArrayList<>();

    public void addObserver(IReceiverRegistryObserver observer) {
        observers.add(observer);
    }

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
        boolean wasEmpty = receivers.isEmpty();
        receivers.put(key, address);
        if (wasEmpty) {
            observers.forEach(IReceiverRegistryObserver::onReceiverAdded);
        }
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

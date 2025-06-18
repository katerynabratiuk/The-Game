package org.lib.packet_processing.registry;

import java.net.SocketAddress;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SocketAddressRegistry implements ISocketAddressRegistry{
    private final Set<SocketAddress> receivers = ConcurrentHashMap.newKeySet();

    @Override
    public Set<SocketAddress> getAll() {
        return receivers;
    }

    @Override
    public void add(SocketAddress address) {
        receivers.add(address);
    }

    @Override
    public void remove(SocketAddress address) {
        receivers.remove(address);
    }
}

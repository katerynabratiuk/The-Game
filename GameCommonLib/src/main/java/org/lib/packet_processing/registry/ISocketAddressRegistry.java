package org.lib.packet_processing.registry;

import java.net.SocketAddress;
import java.util.Collection;

public interface ISocketAddressRegistry {
    Collection<SocketAddress> getAll();
    void add(String key, SocketAddress address);
    void remove(String key);
    SocketAddress get(String clientUUID);
}

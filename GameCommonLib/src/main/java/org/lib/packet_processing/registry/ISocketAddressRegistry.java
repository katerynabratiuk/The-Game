package org.lib.packet_processing.registry;

import java.net.SocketAddress;
import java.util.Set;

public interface ISocketAddressRegistry {
    Set<SocketAddress> getAll();
    void add(SocketAddress address);
    void remove(SocketAddress address);
}

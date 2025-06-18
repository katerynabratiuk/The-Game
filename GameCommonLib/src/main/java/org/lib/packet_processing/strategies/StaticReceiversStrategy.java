package org.lib.packet_processing.strategies;

import java.net.SocketAddress;
import java.util.Set;

public class StaticReceiversStrategy implements ReceiverStrategy {
    private final Set<SocketAddress> receivers;

    public StaticReceiversStrategy(Set<SocketAddress> receivers) {
        this.receivers = receivers;
    }

    @Override
    public Set<SocketAddress> getReceivers() {
        return receivers;
    }
}
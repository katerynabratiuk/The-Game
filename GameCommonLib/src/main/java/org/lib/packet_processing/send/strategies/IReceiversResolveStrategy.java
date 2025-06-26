package org.lib.packet_processing.send.strategies;

import org.lib.data.payloads.NetworkPayload;

import java.net.SocketAddress;
import java.util.Collection;

public interface IReceiversResolveStrategy {
    Collection<SocketAddress> resolveReceivers(NetworkPayload payload);
}
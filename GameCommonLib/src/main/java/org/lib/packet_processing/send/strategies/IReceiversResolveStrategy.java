package org.lib.packet_processing.send.strategies;

import org.lib.data_structures.payloads.NetworkPayload;

import java.net.SocketAddress;
import java.util.Collection;

public interface IReceiversResolveStrategy {
    Collection<SocketAddress> resolveReceivers(NetworkPayload payload);
}
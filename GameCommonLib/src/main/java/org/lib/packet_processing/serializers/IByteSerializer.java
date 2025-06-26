package org.lib.packet_processing.serializers;

import org.lib.data.payloads.NetworkPayload;

public interface IByteSerializer {
    static byte[] serialize(NetworkPayload payload) { return new byte[0]; }
    static NetworkPayload deserialize(byte[] data) { return new NetworkPayload(); }
}

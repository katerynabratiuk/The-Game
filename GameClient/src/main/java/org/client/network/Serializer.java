package org.client.network;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.lib.DataStructures.payloads.NetworkPayload;


public class Serializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static byte[] serialize(NetworkPayload payload) {
        return objectMapper.writeValueAsBytes(payload);
    }

    @SneakyThrows
    public static NetworkPayload deserialize(byte[] data) {
        return objectMapper.readValue(data, NetworkPayload.class);
    }
}

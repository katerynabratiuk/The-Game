package org.server.network;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lib.data_structures.payloads.NetworkPayload;

import java.io.IOException;


public class Serializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static byte[] serialize(NetworkPayload payload) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(payload);
    }


    public static NetworkPayload deserialize(byte[] data) throws IOException {
        return objectMapper.readValue(data, NetworkPayload.class);
    }
}

package org.client.network;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.lib.data_structures.payloads.NetworkPayload;

import java.io.IOException;


public class Serializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static byte[] serialize(NetworkPayload payload) {

        // temp try catch
        try {
            return objectMapper.writeValueAsBytes(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public static NetworkPayload deserialize(byte[] data) {
        try {
            return objectMapper.readValue(data, NetworkPayload.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

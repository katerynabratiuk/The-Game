package org.lib.packet_processing;

import org.junit.jupiter.api.Test;
import org.lib.data.payloads.NetworkPayload;
import org.lib.data.payloads.enums.PayloadStructType;
import org.lib.data.payloads.game.Notification;
import org.lib.packet_processing.serializers.BsonSerializer;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SerializerTest {
    @Test
    void testSerializeAndDeserialize() throws IOException {
        var payload = new Notification("test");
        String uuid = UUID.randomUUID().toString();
        var original = new NetworkPayload(List.of(payload), uuid);

        byte[] serialized = BsonSerializer.serialize(original);
        NetworkPayload deserialized = BsonSerializer.deserialize(serialized);

        assertEquals(original.getClientUUID(), deserialized.getClientUUID());
        assertEquals(1, deserialized.getPayloads().size());
        assertEquals(PayloadStructType.NOTIFICATION, deserialized.getPayloads().get(0).getType());
    }

    @Test
    void testDeserializeInvalidDataThrowsException() {
        byte[] invalidData = {0x01, 0x02, 0x03};

        assertThrows(IOException.class, () -> {
            BsonSerializer.deserialize(invalidData);
        });
    }
}

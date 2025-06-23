package org.client.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.client.game_logic.PayloadRouter;
import org.lib.data_structures.payloads.*;
import org.lib.packet_processing.serializers.Serializer;

import java.io.IOException;
import java.util.List;

public class PacketsSenderService {
    private final UDPSocketThread clientThread;

    public PacketsSenderService(PayloadRouter controller) throws IOException {
        this.clientThread = new UDPSocketThread(controller);
    }

    public void start() {
        clientThread.start();
    }

    public void sendInput(PlayerInput input) {
        send(List.of(input));
    }

    public void sendJoinRequest() {
        var joinRequest = new ConnectionRequest(getClientId(), ConnectionCode.JOIN);
        send(List.of(joinRequest));
    }

    public void sendDisconnectRequest() {
        var req = new ConnectionRequest(getClientId(), ConnectionCode.DISCONNECT);
        send(List.of(req));
    }

    public void shutdown() {
        clientThread.shutdown();
    }

    public String getClientId() {
        return clientThread.getClientId();
    }

    private void send(List<Payload> payloads) {
        var serialized = serializePayload(payloads);
        clientThread.getSenderThread().send(serialized);
    }

    private byte[] serializePayload(List<Payload> payloads) {
        var payload = new NetworkPayload(payloads);
        payload.setClientUUID(getClientId());
        try {
            return Serializer.serialize(payload);
        } catch (JsonProcessingException e) {
            // TODO: fix exception throwing
            throw new RuntimeException(e);
        }
    }
}

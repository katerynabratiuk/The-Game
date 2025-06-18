package org.client.network;

import org.client.game_logic.ClientController;
import org.lib.data_structures.payloads.JoinRequest;
import org.lib.data_structures.payloads.NetworkPayload;
import org.lib.data_structures.payloads.PlayerInput;

import java.io.IOException;
import java.util.List;

public class UDPThreadWrapper {
    private final UDPClientThread clientThread;

    public UDPThreadWrapper(ClientController controller) throws IOException {
        this.clientThread = new UDPClientThread(controller);
    }

    public void start() {
        clientThread.start();
    }

    public void sendInput(PlayerInput input) {
        var networkPayload = new NetworkPayload(List.of(input));
        var serialized = Serializer.serialize(networkPayload);
        clientThread.getSenderThread().send(serialized);
    }

    public void sendJoinRequest() {
        var joinRequest = new JoinRequest(getClientId());
        var payload = new NetworkPayload(List.of(joinRequest));
        var serialized = Serializer.serialize(payload);
        clientThread.getSenderThread().send(serialized);
    }

    public void shutdown() {
        clientThread.shutdown();
    }

    public String getClientId() {
        return clientThread.getClientId();
    }
}

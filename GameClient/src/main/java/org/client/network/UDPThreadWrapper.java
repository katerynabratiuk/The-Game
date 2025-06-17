package org.client.network;

import org.client.gameLogic.ClientController;
import org.lib.DataStructures.payloads.NetworkPayload;
import org.lib.DataStructures.payloads.PlayerInput;

import java.io.IOException;
import java.util.List;

public class UDPThreadWrapper {
    private UDPClientThread clientThread;

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

    public void shutdown() {
        clientThread.shutdown();
    }

    public String getClientId() {
        return clientThread.getClientId();
    }
}

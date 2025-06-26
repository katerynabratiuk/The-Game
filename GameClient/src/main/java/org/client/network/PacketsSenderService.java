package org.client.network;

import org.lib.data.payloads.*;
import org.lib.data.payloads.enums.ConnectionCode;
import org.lib.data.payloads.game.PlayerInput;
import org.lib.data.payloads.network.ConnectionRequest;
import org.lib.data.payloads.queries.*;
import org.lib.data.payloads.queries.search.CharacterFilterPayload;

import java.io.IOException;
import java.util.List;


public class PacketsSenderService {
    private final UDPSocketThread clientThread;

    public PacketsSenderService(UDPSocketThread thread) throws IOException {
        this.clientThread = thread;
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

    public void sendRegister(String username, String password) {
        RegisterPayload payload = new RegisterPayload(username, password, getClientId());
        payload.setClientUUID(clientThread.getClientId());
        send(List.of(payload));
    }

    public void sendLogin(String username, String password) {
        LoginPayload payload = new LoginPayload(username, password);
        payload.setClientUUID(clientThread.getClientId());
        send(List.of(payload));
    }

    public void sendCharacterListRequest() {
        CharacterListPayload request = new CharacterListPayload();
        request.setClientUUID(clientThread.getClientId());
        send(List.of(request));
    }

    public void sendCharacterFilterRequest(String name, Boolean fast, Boolean armor) {
        CharacterFilterPayload request = new CharacterFilterPayload(name, fast, armor);
        request.setClientUUID(clientThread.getClientId());
        send(List.of(request));
    }

    public void sendWeaponListRequest() {
        WeaponListPayload request = new WeaponListPayload();
        request.setClientUUID(clientThread.getClientId());
        send(List.of(request));
    }

    public void sendPowerUpRequest() {
        PowerUpListPayload request = new PowerUpListPayload();
        request.setClientUUID(clientThread.getClientId());
        send(List.of(request));
    }


    public void sendUserPickAndJoin(Integer character, Integer weapon, Integer item)
    {
        UserPickPayload payload = new UserPickPayload(character, weapon, item, clientThread.getClientId());
        send(List.of(payload));
    }


    public void shutdown() {
        clientThread.shutdown();
    }

    public String getClientId() {
        return clientThread.getClientId();
    }



    private void send(List<Payload> payloads) {
        if (clientThread.getSenderThread() == null) {
            System.err.println("Sender thread not initialized yet");
            return;
        }
        var payload = new NetworkPayload(payloads);
        payload.setClientUUID(getClientId());
        clientThread.getSenderThread().send(payload);
    }


}

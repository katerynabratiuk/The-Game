package org.server.game_logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.lib.data_structures.concurrency.ConcurrentQueue;
import org.lib.data_structures.payloads.*;
import org.lib.controllers.IRouter;
import org.lib.data_structures.payloads.actors.Actor;
import org.lib.data_structures.payloads.actors.PlayerCharacter;
import org.lib.data_structures.payloads.game.Coordinates;
import org.lib.data_structures.payloads.game.GameState;
import org.lib.data_structures.payloads.game.Notification;
import org.lib.data_structures.payloads.game.PlayerInput;
import org.lib.data_structures.payloads.network.ConnectionRequest;
import org.lib.data_structures.payloads.queries.LoginPayload;
import org.lib.data_structures.payloads.queries.RegisterPayload;
import org.lib.packet_processing.send.BroadcastThread;
import org.lib.packet_processing.send.UnicastThread;

import java.util.List;

public class PayloadRouter implements IRouter, Runnable {
    private final ConcurrentQueue<NetworkPayload> receivedPackets = new ConcurrentQueue<>();
    private final GameStateManager gameStateManager;
    @Getter @Setter private BroadcastThread broadcastThread;
    @Getter @Setter private UnicastThread unicastThread;

    public PayloadRouter(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    @Override
    public void register(NetworkPayload payload) {
        receivedPackets.put(payload);
    }

    @Override
    public void run() {
        while (true) {
            try {
                NetworkPayload payload = receivedPackets.get();
                handle(payload);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }


    @SneakyThrows
    public void handle(NetworkPayload payload) {
        for (var p: payload.getPayloads()) {
            switch (p.getType()) {
                case PLAYER_INPUT -> handlePlayerInput((PlayerInput) p);
                case ACTOR -> handleActor((Actor) p);
                case GAME_STATE -> handleGameState((GameState) p);
                case NOTIFICATION -> handlePlayerNotification((Notification) p);
                case CONNECTION_REQUEST -> handleConnectionRequest((ConnectionRequest) p);
                case REGISTER -> handleRegister((RegisterPayload) p);
                case LOGIN -> handleLogin((LoginPayload) p);
                default -> System.err.println("Unknown payload type: " + p);
            }
        }
    }

    private void handleConnectionRequest(ConnectionRequest p) throws JsonProcessingException {
        switch (p.getConnectionCode()) {
            case JOIN:
                var character = new PlayerCharacter(p.getClientUUID(), new Coordinates(0, 0));
                gameStateManager.addActor(character);
                var gameState = gameStateManager.snapshot();
                var networkPayload = new NetworkPayload(List.of(gameState));
                broadcastThread.send(networkPayload);
                break;

            case DISCONNECT:
                gameStateManager.removeActor(p.getClientUUID());
                broadcastThread.removeReceiver(p.getClientUUID());
        }
    }

    private void handlePlayerNotification(Notification notification) {
        System.out.println("handlePlayerNotification " + notification);
    }

    private void handleRegister(RegisterPayload query) {
        System.out.println("hangleRegister " + query);
        var notif = new Notification("hi there");
        unicastThread.send(new NetworkPayload(List.of(notif), query.getClientUUID()));
    }

    private void handleGameState(GameState p) {
        System.out.println("handleGameState " + p);
    }

    private void handleActor(Actor p) {
        System.out.println("handleActor " + p);
    }

    // TODO: refactor later to decouple sending logic
    private void handlePlayerInput(PlayerInput p) {
        if (broadcastThread == null) {
            System.out.println("Can`t process received message - sender thread is not initialized");
            return;
        }

        gameStateManager.updateGameStateByInput(p);
        var gameState = gameStateManager.snapshot();
        var networkPayload = new NetworkPayload(List.of(gameState));
        broadcastThread.send(networkPayload);
    }
}

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
import org.lib.data_structures.payloads.queries.RegisterPayload;
import org.lib.packet_processing.send.BroadcastThread;
import org.lib.packet_processing.send.UnicastThread;
import org.server.db.repository.implementation.UserRepositoryImpl;
import org.server.db.service.UserService;

import java.util.List;

public class PayloadRouter implements IRouter, Runnable {
    private final ConcurrentQueue<NetworkPayload> receivedPackets = new ConcurrentQueue<>();
    private final GameStateManager gameStateManager;
    @Getter @Setter private BroadcastThread broadcastThread;
    @Getter @Setter private UnicastThread unicastThread;

    // temporary
    private UserService userService = new UserService(new UserRepositoryImpl());

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
                case REGISTER -> hangleRegister((RegisterPayload) p);
                default -> System.err.println("Unknown payload type: " + p);
            }
        }
    }

    private void handleConnectionRequest(ConnectionRequest p) throws JsonProcessingException {
        switch (p.getConnectionCode()) {
            case JOIN:
                String username = gameStateManager.getUsernameByClientUUID(p.getClientUUID());
                var character = new PlayerCharacter(p.getClientUUID(), new Coordinates(0, 0), username);
                gameStateManager.addActor(character);
                break;

            case DISCONNECT:
                gameStateManager.removeActor(p.getClientUUID());
                broadcastThread.removeReceiver(p.getClientUUID());
        }
    }

    private void handlePlayerNotification(Notification notification) {
        System.out.println("handlePlayerNotification " + notification);
    }

    private void hangleRegister(RegisterPayload registerPayload) {
        if (registerPayload.getClientUUID() != null && registerPayload.getUsername() != null) {
            gameStateManager.registerUsername(registerPayload.getClientUUID(), registerPayload.getUsername());
        }
        var notif = new Notification("Welcome to the map!");
        unicastThread.send(new NetworkPayload(List.of(notif), registerPayload.getClientUUID()));
        System.out.println("Registered " + registerPayload.getUsername());

    }

    private void handleGameState(GameState p) {
        System.out.println("handleGameState " + p);
    }

    private void handleActor(Actor p) {
        System.out.println("handleActor " + p);
    }

    private void handlePlayerInput(PlayerInput input) {
        if (broadcastThread == null) {
            System.out.println("Can`t process received message - sender thread is not initialized");
            return;
        }

        KeyBindingsHandler.updateKeyState(input.getClientUUID(), input.getKeyInputCode(), !input.isKeyReleased());
        Actor actor = gameStateManager.getPlayerCharacterByUUID(input.getClientUUID());
        if (actor == null) return;

        KeyBindingsHandler.processInput(actor, gameStateManager.getAllActors(), input);
        var gameState = gameStateManager.snapshot();
        broadcastThread.send(new NetworkPayload(List.of(gameState)));
    }
}

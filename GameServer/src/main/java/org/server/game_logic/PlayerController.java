package org.server.game_logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.lib.data_structures.concurrency.ConcurrentQueue;
import org.lib.data_structures.payloads.*;
import org.lib.controllers.IController;
import org.lib.data_structures.payloads.actors.Actor;
import org.lib.packet_processing.registry.SocketAddressRegistry;
import org.lib.packet_processing.send.PacketSenderThread;
import org.lib.packet_processing.serializers.Serializer;

import java.io.IOException;
import java.util.List;

public class PlayerController implements IController, Runnable {
    private final ConcurrentQueue<NetworkPayload> receivedPackets = new ConcurrentQueue<>();
    private final GameStateService gameStateService;
    @Getter @Setter private PacketSenderThread senderThread;

    public PlayerController(GameStateService gameStateService) {
        this.gameStateService = gameStateService;
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
    private void handle(NetworkPayload payload) {
        for (var p: payload.getPayloads()) {
            switch (p.getType()) {
                case PLAYER_INPUT -> handlePlayerInput((PlayerInput) p);
                case ACTOR -> handleActor((Actor) p);
                case GAME_STATE -> handleGameState((GameState) p);
                case PLAYER_NOTIFICATION -> handlePlayerNotification((PlayerNotification) p);
                case CONNECTION_REQUEST -> handleConnectionRequest((ConnectionRequest) p);
                default -> System.err.println("Unknown payload type: " + p);
            }
        }
    }

    private void handleConnectionRequest(ConnectionRequest p) throws JsonProcessingException {
        // WIP
        // add check if this client is already present in the game state

        switch (p.getConnectionCode()) {
            case JOIN:
                Actor newActor = new Actor(new Coordinates(0, 0), p.getClientUUID());
                gameStateService.addActor(newActor);
                var gameState = gameStateService.snapshot();
                var networkPayload = new NetworkPayload(List.of(gameState));
                var serialized = Serializer.serialize(networkPayload);
                senderThread.send(serialized);
                break;

            case DISCONNECT:
                gameStateService.removeActor(p.getClientUUID());
                senderThread.removeReceiver(p.getClientUUID());
        }
    }

    private void handlePlayerNotification(PlayerNotification notification) {
        System.out.println("handlePlayerNotification " + notification);
    }

    private void handleGameState(GameState p) {
        System.out.println("handleGameState " + p);
    }

    private void handleActor(Actor p) {
        System.out.println("handleActor " + p);
    }

    // TODO: refactor later to decouple sending logic
    private void handlePlayerInput(PlayerInput p) throws JsonProcessingException {
        if (senderThread == null) {
            System.out.println("Can`t process received message - sender thread is not initialized");
            return;
        }

        gameStateService.updateGameStateByInput(p);
        var gameState = gameStateService.snapshot();
        var networkPayload = new NetworkPayload(List.of(gameState));
        var serialized = Serializer.serialize(networkPayload);
        senderThread.send(serialized);
    }
}

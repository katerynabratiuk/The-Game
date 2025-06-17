package org.server.gameLogic;

import lombok.SneakyThrows;
import org.lib.DataStructures.concurrency.ConcurrentQueue;
import org.lib.DataStructures.payloads.*;
import org.lib.GameControllers.IController;
import org.server.network.Serializer;

public class PlayerController implements IController, Runnable {
    private final ConcurrentQueue<NetworkPayload> receivedPackets = new ConcurrentQueue<>();

    @Override
    public void register(byte[] payload) {
        var serialized = Serializer.deserialize(payload);
        receivedPackets.put(serialized);
    }

    @Override
    public void run() {
        while (true) {
            try {
                NetworkPayload payload = receivedPackets.get();
                handle(payload);
            } catch (Exception e) {
                e.printStackTrace();
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
                default -> System.err.println("Unknown payload type: " + p);
            }
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

    private void handlePlayerInput(PlayerInput p) {
        System.out.println("handlePlayerInput " + p);
    }
}

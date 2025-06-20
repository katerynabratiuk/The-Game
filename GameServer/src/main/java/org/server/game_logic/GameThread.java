package org.server.game_logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Setter;
import org.lib.data_structures.payloads.NetworkPayload;
import org.lib.packet_processing.send.PacketSenderThread;
import org.lib.packet_processing.serializers.Serializer;

import java.util.List;

public class GameThread extends Thread {
    private final GameStateService gameStateService;
    @Setter private PacketSenderThread senderThread;
    private final int FPS = 30; // dev test value
    private final long FRAME_TIME = 1000 / FPS;

    public GameThread(GameStateService gameStateService) {
        this.gameStateService = gameStateService;
    }


    @Override
    public void run() {
        while (true) {
            if (senderThread == null || !senderThread.isAlive()) break;

            // TODO: prototype logic
            if (!senderThread.hasReceivers()) {
                System.out.println("No receivers. Waiting...");
                try {
                    Thread.sleep(1000);
                    continue;
                } catch (InterruptedException e) {
                    break;
                }
            }

            try {
                long startTime = System.currentTimeMillis();

                gameStateService.updateGameThread(); // move bullets, calc collisions, etc

                var gameState = gameStateService.snapshot();
                var payload = new NetworkPayload(List.of(gameState));
                var serialized = Serializer.serialize(payload);
                senderThread.send(serialized);

                // maintain frame rate - verify this
                long elapsed = System.currentTimeMillis() - startTime;
                long sleepTime = FRAME_TIME - elapsed;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException | JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
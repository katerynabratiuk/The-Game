package org.server.game_logic;

import lombok.Setter;
import org.lib.data_structures.payloads.NetworkPayload;
import org.lib.packet_processing.send.SenderThread;

import java.util.List;

public class GameThread extends Thread {
    private final GameStateManager gameStateService;
    @Setter private SenderThread broadcastThread;
    @Setter private SenderThread unicastThread;
    private final int FPS = 30;
    private final long FRAME_TIME = 1000 / FPS;

    public GameThread(GameStateManager gameStateManager) {
        this.gameStateService = gameStateManager;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (shouldSkipFrame()) continue;
                executeGameFrame();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("GameThread interrupted, stopping...");
                break;
            }
        }
    }

    private boolean shouldSkipFrame() throws InterruptedException {
        if (broadcastThread == null) return true;
        if (!broadcastThread.noReceivers()) return false;

        System.out.println("No receivers. Waiting...");
        broadcastThread.waitForReceivers();
        return true;
    }

    private void executeGameFrame() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        gameStateService.updateGameThread(unicastThread, broadcastThread);
        var gameState = gameStateService.snapshot();
        broadcastThread.send(new NetworkPayload(List.of(gameState)));

        maintainFrameRate(startTime);
    }

    private void maintainFrameRate(long startTime) throws InterruptedException {
        long elapsed = System.currentTimeMillis() - startTime;
        long sleepTime = FRAME_TIME - elapsed;
        if (sleepTime > 0) {
            Thread.sleep(sleepTime);
        }
    }
}

package org.server.game_logic;

import lombok.Setter;
import org.lib.data_structures.payloads.NetworkPayload;
import org.lib.packet_processing.send.BroadcastThread;
import org.lib.packet_processing.send.UnicastThread;

import java.util.List;

public class GameThread extends Thread {
    private final GameStateManager gameStateService;
    @Setter private BroadcastThread broadcastThread;
    @Setter private UnicastThread unicastThread;
    private final int FPS = 30;
    private final long FRAME_TIME = 1000 / FPS;
    private static final int MAX_RETRIES = 50;
    private static final int RETRY_DELAY_MS = 100;

    public GameThread(GameStateManager gameStateManager) {
        this.gameStateService = gameStateManager;
    }


    @Override
    public void run() {
        if (broadcastThread == null) {
            System.err.println("GameThread failed to start - broadcastThread not initialized");
            return;
        }

        waitForBroadcastThreadReady();
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

    private void waitForBroadcastThreadReady() {
        int retryCount = 0;
        if (!broadcastThread.isAlive()) {
            while (!broadcastThread.isAlive() && retryCount++ < MAX_RETRIES) {
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("broadcastThread terminated, stopping GameThread");
                    return;
                }
            }
        }
    }

    private boolean shouldSkipFrame() throws InterruptedException {
        if (!broadcastThread.hasReceivers()) {
            System.out.println("No receivers. Waiting...");
            broadcastThread.waitForReceivers();
            return true;
        }
        return false;
    }

    private void executeGameFrame() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        gameStateService.updateGameThread(unicastThread);
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

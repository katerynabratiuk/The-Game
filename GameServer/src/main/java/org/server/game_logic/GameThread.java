package org.server.game_logic;

import lombok.Setter;
import org.lib.data_structures.payloads.NetworkPayload;
import org.lib.packet_processing.send.BroadcastThread;

import java.util.List;

public class GameThread extends Thread {
    private final GameStateManager gameStateService;
    @Setter private BroadcastThread broadcastThread;
    private final int FPS = 30;
    private final long FRAME_TIME = 1000 / FPS;
    private static final int MAX_RETRIES = 50;
    private static final int RETRY_DELAY_MS = 100;
    private static final int SKIP_FRAME_DELAY_MS = 1000;

    public GameThread(GameStateManager gameStateManager) {
        this.gameStateService = gameStateManager;
    }


    @Override
    public void run() {
        if (broadcastThread == null) {
            System.err.println("GameThread failed to start - broadcastThread not initialized");
            return;
        }

        while (true) {
            waitForBroadcastThreadReady();
            if (shouldSkipFrame()) continue;

            try {
                executeGameFrame();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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

    private boolean shouldSkipFrame() {
        if (!broadcastThread.hasReceivers()) {
            System.out.println("No receivers. Waiting...");
            try {
                Thread.sleep(SKIP_FRAME_DELAY_MS);
                return true;
            } catch (InterruptedException e) {
                return false;
            }
        }
        return false;
    }

    private void executeGameFrame() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        gameStateService.updateGameThread();
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

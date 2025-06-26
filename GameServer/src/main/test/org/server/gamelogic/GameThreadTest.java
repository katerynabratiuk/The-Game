package org.server.gamelogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lib.data.payloads.NetworkPayload;
import org.lib.data.payloads.game.GameState;
import org.lib.packet_processing.send.SenderThread;
import org.server.game_logic.GameStateManager;
import org.server.game_logic.GameThread;

import static org.mockito.Mockito.*;


public class GameThreadTest {
    private GameStateManager gameStateManager;
    private SenderThread broadcastThread;
    private SenderThread unicastThread;
    private GameThread gameThread;

    @BeforeEach
    public void setUp() {
        gameStateManager = mock(GameStateManager.class);
        broadcastThread = mock(SenderThread.class);
        unicastThread = mock(SenderThread.class);
        gameThread = new GameThread(gameStateManager);
        gameThread.setBroadcastThread(broadcastThread);
        gameThread.setUnicastThread(unicastThread);
    }

    @Test
    public void testExecuteGameFrame_whenReceiversExist() throws Exception {
        when(broadcastThread.noReceivers()).thenReturn(false);
        when(gameStateManager.snapshot()).thenReturn(new GameState());

        var executeGameFrame = gameThread.getClass().getDeclaredMethod("executeGameFrame");
        executeGameFrame.setAccessible(true);
        executeGameFrame.invoke(gameThread);

        verify(gameStateManager).updateGameThread(unicastThread, broadcastThread);
        verify(broadcastThread).send(any(NetworkPayload.class));

        if (gameThread.isAlive()) {
            gameThread.interrupt();
            gameThread.join();
        }
    }
}

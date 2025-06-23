package org.server;

import org.server.game_logic.GameStateManager;
import org.server.game_logic.GameThread;
import org.server.game_logic.PayloadRouter;
import org.server.network.UDPSockerThread;

public class UDPServer {
    public static void main(String[] args) {
        GameStateManager service = new GameStateManager();
        PayloadRouter controller = new PayloadRouter(service);
        UDPSockerThread serverThread = new UDPSockerThread(controller);
        GameThread gameThread = new GameThread(service);

        serverThread.start();
        gameThread.start();
        new Thread(controller).start();
        controller.setSenderThread(serverThread.getSendingThread());
        gameThread.setSenderThread(serverThread.getSendingThread());
    }
}

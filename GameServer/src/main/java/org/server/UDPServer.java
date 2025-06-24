package org.server;

import org.server.game_logic.GameStateManager;
import org.server.game_logic.GameThread;
import org.server.game_logic.PayloadRouter;
import org.server.network.UDPSocketThread;

public class UDPServer {
    public static void main(String[] args) {
        var service = new GameStateManager();
        var router = new PayloadRouter(service);
        var serverThread = new UDPSocketThread(router);
        var gameThread = new GameThread(service);

        router.setBroadcastThread(serverThread.getBroadcastThread());
        router.setUnicastThread(serverThread.getUnicastThread());
        gameThread.setBroadcastThread(serverThread.getBroadcastThread());

        serverThread.start();
        gameThread.start();
        new Thread(router).start();
    }
}

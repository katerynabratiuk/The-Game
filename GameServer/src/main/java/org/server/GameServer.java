package org.server;

import org.server.game_logic.*;
import org.server.network.UDPSocketThread;

public class GameServer {
    public static void main(String[] args) {
        setupComponents();
    }

    private static void setupComponents() {
        var service = new GameStateManager(new PlayersStatsTracker(), new CollisionHandler(), new BulletUpdateHandler());
        var router = new PayloadRouter(service);
        var serverThread = new UDPSocketThread(router);
        var gameThread = new GameThread(service);

        router.setBroadcastThread(serverThread.getBroadcastThread());
        router.setUnicastThread(serverThread.getUnicastThread());
        gameThread.setBroadcastThread(serverThread.getBroadcastThread());
        gameThread.setUnicastThread(serverThread.getUnicastThread());

        serverThread.start();
        gameThread.start();
        new Thread(router).start();
    }
}

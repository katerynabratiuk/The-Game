package org.server;

import org.server.game_logic.GameStateService;
import org.server.game_logic.PlayerController;
import org.server.network.UDPServerThread;

public class UDPServer {
    public static void main(String[] args) {
        PlayerController controller = new PlayerController(new GameStateService());
        UDPServerThread serverThread = new UDPServerThread(controller);

        serverThread.start();
        new Thread(controller).start();
        controller.setSenderThread(serverThread.getSendingThread());
    }
}

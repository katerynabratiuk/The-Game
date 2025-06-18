package org.server;

import org.server.game_logic.GameStateService;
import org.server.game_logic.GameThread;
import org.server.game_logic.PlayerController;
import org.server.network.UDPServerThread;

public class UDPServer {
    public static void main(String[] args) {
        GameStateService service = new GameStateService();
        PlayerController controller = new PlayerController(service);
        UDPServerThread serverThread = new UDPServerThread(controller);
        GameThread gameThread = new GameThread(service);

        serverThread.start();
        gameThread.start();
        new Thread(controller).start();
        controller.setSenderThread(serverThread.getSendingThread());
        gameThread.setSenderThread(serverThread.getSendingThread());
    }
}

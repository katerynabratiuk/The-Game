package org.server;

import org.server.gameLogic.PlayerController;
import org.server.network.UDPServerThread;

public class UDPServer {
    public static void main(String[] args) throws Exception {
        PlayerController controller = new PlayerController();

        new UDPServerThread(controller).start(); // Pass to UDP
        new Thread(controller).start();
    }
}

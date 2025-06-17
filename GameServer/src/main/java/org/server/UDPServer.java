package org.server;

import org.server.network.UDPServerThread;

public class UDPServer {
    public static void main(String[] args) throws Exception {
        new UDPServerThread().start();
    }
}

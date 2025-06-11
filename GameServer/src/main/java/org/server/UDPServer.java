package org.server;

public class UDPServer {
    public static void main(String[] args) throws Exception {
        new UDPServerThread().start();
    }
}

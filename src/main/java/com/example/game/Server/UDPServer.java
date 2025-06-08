package com.example.game.Server;

public class UDPServer {
    public static void main(String[] args) throws Exception {
        new UDPServerThread().run();
    }
}

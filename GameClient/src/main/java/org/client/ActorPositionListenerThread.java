package org.client;

import org.client.UI.ActorPanel;
import org.lib.DataStructures.GameState;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ActorPositionListenerThread extends Thread {
    private final DatagramSocket socket;
    private final int BUFFER_SIZE = 1024;
    private final ActorPanel actorPanel;
    private volatile boolean running = true;

    public ActorPositionListenerThread(DatagramSocket socket, ActorPanel actorPanel) {
        this.socket = socket;
        setDaemon(true);
        this.actorPanel = actorPanel;
    }

    public void shutdown() {
        running = false;
        interrupt();
        socket.close();
    }

    @Override
    public void run() {
        System.out.println("ActorPositionListenerThread started");
        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
                socket.receive(packet);
                process(packet);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Listener error: " + e.getMessage());
                break;
            }
        }
    }


    private void process(DatagramPacket packet) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
        GameState gameState = (GameState) ois.readObject();
        SwingUtilities.invokeLater(() -> actorPanel.updateGameState(gameState));
        System.out.println("Received broadcast");
    }
}
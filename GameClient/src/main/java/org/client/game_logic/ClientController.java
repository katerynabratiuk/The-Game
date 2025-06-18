package org.client.game_logic;

import lombok.Setter;
import org.client.UI.ActorPanel;
import org.client.network.Serializer;
import org.client.network.UDPThreadWrapper;
import org.lib.data_structures.concurrency.ConcurrentQueue;
import org.lib.data_structures.payloads.*;
import org.lib.controllers.IController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ClientController implements IController, Runnable {
    private final ActorPanel actorPanel;
    private final ConcurrentQueue<NetworkPayload> receivedPackets = new ConcurrentQueue<>();
    @Setter private UDPThreadWrapper networkManager;

    public ClientController(ActorPanel actorPanel) {
        this.actorPanel = actorPanel;
    }

    public KeyListener getKeyListener(JLabel positionLabel) {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                PlayerInput input = new PlayerInput(networkManager.getClientId(), e.getKeyCode());
                networkManager.sendInput(input);
                positionLabel.setText("Last Key: " + e.getKeyCode());
            }
        };
    }

    public MouseListener getMouseClickListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();

                int mapX = clickPoint.x;
                int mapY = clickPoint.y;

                Coordinates target = new Coordinates(mapX, mapY);
                PlayerInput input = new PlayerInput(networkManager.getClientId(), MouseEvent.BUTTON1);
                input.setTargetCoordinates(target);

                System.out.println("Mouse clicked at: " + input.getTargetCoordinates().getX() + " " + input.getTargetCoordinates().getY());

                networkManager.sendInput(input);
            }
        };
    }

    @Override
    public void register(byte[] payload) {
        var serialized = Serializer.deserialize(payload);
        receivedPackets.put(serialized);
    }

    @Override
    public void run() {
        while (true) {
            try {
                NetworkPayload payload = receivedPackets.get();
                handle(payload);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handle(NetworkPayload payload) {
        for (var p : payload.getPayloads()) {
            switch (p.getType()) {
                case GAME_STATE -> handleGameState((GameState) p);
                case PLAYER_NOTIFICATION -> handlePlayerNotification((PlayerNotification) p);
                default -> System.err.println("Unknown payload type: " + p);
            }
        }
    }

    private void handlePlayerNotification(PlayerNotification p) {
    }

    private void handleGameState(GameState p) {
        actorPanel.updateGameState(p);
    }
}


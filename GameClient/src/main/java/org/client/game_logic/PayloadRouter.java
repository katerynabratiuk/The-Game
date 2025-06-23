package org.client.game_logic;

import lombok.Setter;
import org.client.UI.MapDisplayManager;
import org.client.network.PacketsSenderService;
import org.lib.data_structures.concurrency.ConcurrentQueue;
import org.lib.data_structures.payloads.*;
import org.lib.controllers.IRouter;
import org.lib.data_structures.payloads.game.GameState;
import org.lib.data_structures.payloads.game.Notification;
import org.lib.data_structures.payloads.game.PlayerInput;
import org.lib.data_structures.payloads.game.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class PayloadRouter implements IRouter, Runnable {
    private final MapDisplayManager mapDisplayManager;
    private final ConcurrentQueue<NetworkPayload> receivedPackets = new ConcurrentQueue<>();
    @Setter private PacketsSenderService networkManager;

    public PayloadRouter(MapDisplayManager mapDisplayManager) {
        this.mapDisplayManager = mapDisplayManager;
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
                var direction = new Vector(mapDisplayManager.convertToGameCoordinates(clickPoint.x, clickPoint.y));
                PlayerInput input = new PlayerInput(networkManager.getClientId(), MouseEvent.BUTTON1);
                input.setDirection(direction);

                System.out.println("Mouse clicked at: " + input.getDirection().getX() + " " + input.getDirection().getY());
                networkManager.sendInput(input);
            }
        };
    }

    @Override
    public void register(NetworkPayload payload) {
        receivedPackets.put(payload);
    }

    @Override
    public void run() {
        while (true) {
            try {
                NetworkPayload payload = receivedPackets.get();
                handle(payload);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void handle(NetworkPayload payload) {
        for (var p : payload.getPayloads()) {
            switch (p.getType()) {
                case GAME_STATE -> handleGameState((GameState) p);
                case NOTIFICATION -> handlePlayerNotification((Notification) p);
                default -> System.err.println("Unknown payload type: " + p);
            }
        }
    }

    private void handlePlayerNotification(Notification p) {
        System.out.println("Received notification - " + p.getMessage());
    }

    private void handleGameState(GameState p) {
        mapDisplayManager.updateGameState(p);
    }
}


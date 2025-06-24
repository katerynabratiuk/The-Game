package org.client.game_logic;

import lombok.Setter;
import org.client.UI.MapPanel;
import org.client.UI.PopupRenderer;
import org.client.network.PacketsSenderService;
import org.lib.data_structures.concurrency.ConcurrentQueue;
import org.lib.data_structures.payloads.*;
import org.lib.controllers.IRouter;
import org.lib.data_structures.payloads.game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class PayloadRouter implements IRouter, Runnable {
    private final MapPanel mapPanel;
    private final ConcurrentQueue<NetworkPayload> receivedPackets = new ConcurrentQueue<>();
    @Setter private PacketsSenderService packetsSenderService;
    private volatile boolean isKilled = false;

    public PayloadRouter(MapPanel mapPanel) {
        this.mapPanel = mapPanel;
    }

    public KeyListener getKeyListener(JLabel positionLabel) {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isKilled) return; // Disable input if killed
                PlayerInput input = new PlayerInput(packetsSenderService.getClientId(), e.getKeyCode());
                packetsSenderService.sendInput(input);
            }
        };
    }

    public MouseListener getMouseClickListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isKilled) return; // Disable input if killed
                Point clickPoint = e.getPoint();
                var direction = new Vector(mapPanel.convertToGameCoordinates(clickPoint.x, clickPoint.y));
                PlayerInput input = new PlayerInput(packetsSenderService.getClientId(), MouseEvent.BUTTON1);
                input.setDirection(direction);

                System.out.println("Mouse clicked at: " + input.getDirection().getX() + " " + input.getDirection().getY());
                packetsSenderService.sendInput(input);
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

    private void handlePlayerNotification(Notification notif) {
        PopupRenderer.renderNotification(notif, mapPanel);
    }

    private void handleGameState(GameState p) {
        mapPanel.updateGameState(p);
    }
}


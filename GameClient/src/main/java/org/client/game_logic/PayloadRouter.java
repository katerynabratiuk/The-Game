package org.client.game_logic;

import lombok.Setter;
import org.client.UI.InputCallback;
import org.client.UI.MapPanel;
import org.client.UI.PopupRenderer;
import org.client.network.PacketsSenderService;
import org.lib.data_structures.concurrency.ConcurrentQueue;
import org.lib.data_structures.payloads.*;
import org.lib.controllers.IRouter;
import org.lib.data_structures.payloads.game.*;
import org.lib.data_structures.payloads.enums.NotificationCode;

import java.awt.event.*;


public class PayloadRouter implements IRouter, Runnable, InputCallback {
    private final MapPanel mapPanel;
    private final ConcurrentQueue<NetworkPayload> receivedPackets = new ConcurrentQueue<>();
    @Setter private PacketsSenderService packetsSenderService;

    public PayloadRouter(MapPanel mapPanel) {
        this.mapPanel = mapPanel;
        this.mapPanel.setInputCallback(this);
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

    @Override
    public void onKeyPressed(int keyCode) {
        PlayerInput input = new PlayerInput(packetsSenderService.getClientId(), keyCode);
        packetsSenderService.sendInput(input);
    }

    @Override
    public void onKeyReleased(int keyCode) {
        PlayerInput input = new PlayerInput(packetsSenderService.getClientId(), keyCode);
        input.setKeyReleased(true);
        packetsSenderService.sendInput(input);
    }
    
    @Override
    public void onMouseClicked(int x, int y) {
        var direction = new Vector(new Coordinates(x, y));
        PlayerInput input = new PlayerInput(packetsSenderService.getClientId(), MouseEvent.BUTTON1);
        input.setDirection(direction);
        
        System.out.println("Mouse clicked at: " + input.getDirection().getX() + " " + input.getDirection().getY());
        packetsSenderService.sendInput(input);
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
        if (notif.getCode() == NotificationCode.KILL) {
            mapPanel.onKill();
        }
    }

    private void handleGameState(GameState p) {
        mapPanel.updateGameState(p);
    }
}


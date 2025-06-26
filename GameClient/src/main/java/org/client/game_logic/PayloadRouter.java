package org.client.game_logic;

import lombok.Setter;
import org.client.Startup;
import org.client.UI.InputCallback;
import org.client.UI.MapPanel;
import org.client.UI.PopupRenderer;
import org.client.UI.UIProvider;
import org.client.network.PacketsSenderService;
import org.lib.data_structures.concurrency.ConcurrentQueue;
import org.lib.data_structures.payloads.*;
import org.lib.controllers.IRouter;
import org.lib.data_structures.payloads.actors.Coordinates;
import org.lib.data_structures.payloads.game.*;
import org.lib.data_structures.payloads.enums.NotificationCode;
import org.lib.data_structures.payloads.queries.CharacterListPayload;
import org.lib.data_structures.payloads.queries.PowerUpListPayload;
import org.lib.data_structures.payloads.queries.WeaponListPayload;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;


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
                route(payload);
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

    public void route(NetworkPayload payload) {
        for (var p : payload.getPayloads()) {
            switch (p.getType()) {
                case GAME_STATE -> handleGameState((GameState) p);
                case NOTIFICATION -> handlePlayerNotification((Notification) p);
                case CHARACTER_LIST -> handleCharacterList((CharacterListPayload)p);
                case WEAPON_LIST -> handleWeaponList((WeaponListPayload) p);
                case POWERUP_LIST -> handlePowerUpList((PowerUpListPayload) p);
                default -> System.err.println("Unknown payload type: " + p);
            }
        }
    }

    private void handlePowerUpList(PowerUpListPayload payload) {
        System.out.println("Received weapons: " + payload.getItemList());

        SwingUtilities.invokeLater(() -> {
            UIProvider.displayPowerUpSelection(Startup.getMainFrame(), new ArrayList<>(payload.getItemList()));
        });
    }

    private void handleCharacterList(CharacterListPayload payload) {
        System.out.println("Received characters: " + payload.getCharacters());

        SwingUtilities.invokeLater(() -> {
            UIProvider.displayCharacterSelection(Startup.getMainFrame(), new ArrayList<>(payload.getCharacters()));
        });
    }

    private void handleWeaponList(WeaponListPayload payload) {
        System.out.println("Received weapons: " + payload.getItemList());

        SwingUtilities.invokeLater(() -> {
            UIProvider.displayWeaponSelection(Startup.getMainFrame(), new ArrayList<>(payload.getItemList()));
        });
    }


    private void handlePlayerNotification(Notification notif) {
        PopupRenderer.renderNotification(notif, mapPanel);
        if (notif.getCode() == NotificationCode.KILL) {
            mapPanel.onKill();
        }
        String msg = notif.getMessage();

        if (msg.equals("Login successful")) {
            Startup.getPacketsSenderService().sendCharacterListRequest();
        } else if (msg.equals("Welcome to the map!")) {
            Startup.getPacketsSenderService().sendCharacterListRequest();
        }
    }

    private void handleGameState(GameState p) {
        mapPanel.updateGameState(p);
    }
}


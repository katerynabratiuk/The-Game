package org.client.game_logic;

import org.client.Startup;
import org.client.UI.MapPanel;
import org.client.UI.PopupRenderer;
import org.client.UI.UIProvider;
import org.lib.data.concurrency.ConcurrentQueue;
import org.lib.data.payloads.*;
import org.lib.controllers.IRouter;
import org.lib.data.payloads.game.*;
import org.lib.data.payloads.enums.NotificationCode;
import org.lib.data.payloads.queries.CharacterListPayload;
import org.lib.data.payloads.queries.PowerUpListPayload;
import org.lib.data.payloads.queries.WeaponListPayload;

import javax.swing.*;
import java.util.ArrayList;


public class PayloadRouter implements IRouter, Runnable {
    private final MapPanel mapPanel;
    private final ConcurrentQueue<NetworkPayload> receivedPackets = new ConcurrentQueue<>();

    public PayloadRouter(MapPanel mapPanel) {
        this.mapPanel = mapPanel;
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
                System.err.println(e.getMessage());
                break;
            }
        }
    }

    public void route(NetworkPayload payload) {
        for (var p : payload.getPayloads()) {
            switch (p.getType()) {
                case GAME_STATE -> handleGameState((GameState) p);
                case NOTIFICATION -> handlePlayerNotification((Notification) p);
                case CHARACTER_LIST -> handleCharacterList((CharacterListPayload)p);
                case CHARACTER_FILTER -> handleCharacterFilter((CharacterListPayload)p);
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

    private void handleCharacterFilter(CharacterListPayload payload)
    {
        System.out.println("Received filtered characters: " + payload.getCharacters());
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


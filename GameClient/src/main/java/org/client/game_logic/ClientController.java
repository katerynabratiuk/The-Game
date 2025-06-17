package org.client.game_logic;

import lombok.Setter;
import lombok.SneakyThrows;
import org.client.UI.ActorPanel;
import org.client.network.Serializer;
import org.client.network.UDPThreadWrapper;
import org.lib.data_structures.concurrency.ConcurrentQueue;
import org.lib.data_structures.payloads.GameState;
import org.lib.data_structures.payloads.NetworkPayload;
import org.lib.data_structures.payloads.PlayerInput;
import org.lib.data_structures.payloads.PlayerNotification;
import org.lib.controllers.IController;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class ClientController implements IController, Runnable {
    private final ActorPanel actorPanel;
    private final ConcurrentQueue<NetworkPayload> receivedPackets = new ConcurrentQueue<>();
    @Setter
    private UDPThreadWrapper networkManager; // Will be injected

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

    @SneakyThrows
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
        System.out.println(p);
    }

    private void handleGameState(GameState p) {
        actorPanel.updateGameState(p);
    }
}


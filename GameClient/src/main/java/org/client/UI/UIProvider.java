package org.client.UI;

import lombok.SneakyThrows;
import org.client.game_logic.ClientController;
import org.client.network.Serializer;
import org.client.network.UDPThreadWrapper;
import org.lib.data_structures.payloads.JoinRequest;
import org.lib.data_structures.payloads.NetworkPayload;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class UIProvider {
    private static ActorPanel actorPanel;
    private static UDPThreadWrapper networkManager;
    private static ClientController controller;

    public static void createAndShowGUI() {
        try {
            initSocket();
            createGUI();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to start network: " + e.getMessage());
        }
    }


    private static void initSocket() throws IOException {
        actorPanel = new ActorPanel();
        controller = new ClientController(actorPanel);
        new Thread(controller).start();

        networkManager = new UDPThreadWrapper(controller);
        controller.setNetworkManager(networkManager);
        networkManager.start();
        networkManager.sendJoinRequest();
    }

    private static void createGUI() {
        JFrame frame = new JFrame("Demo UDP Client");
        JLabel positionLabel = new JLabel("Position: (0, 0)");

        actorPanel.setFocusable(true);
        actorPanel.setBackground(Color.LIGHT_GRAY);

        frame.setLayout(new BorderLayout());
        frame.add(positionLabel, BorderLayout.NORTH);
        frame.add(actorPanel, BorderLayout.CENTER);

        actorPanel.addKeyListener(controller.getKeyListener(positionLabel));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
        SwingUtilities.invokeLater(actorPanel::requestFocusInWindow);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                networkManager.shutdown();
            }
        });
    }


}


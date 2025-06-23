package org.client.UI;

import org.client.game_logic.PayloadRouter;
import org.client.network.PacketsSenderService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class UIProvider {
    private static MapDisplayManager mapDisplayManager;
    private static PacketsSenderService networkManager;
    private static PayloadRouter controller;

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
        mapDisplayManager = new MapDisplayManager();
        controller = new PayloadRouter(mapDisplayManager);
        new Thread(controller).start();

        networkManager = new PacketsSenderService(controller);
        controller.setNetworkManager(networkManager);
        networkManager.start();
        networkManager.sendJoinRequest();
    }

    private static void createGUI() {
        JFrame frame = new JFrame("Demo UDP Client");
        JLabel positionLabel = new JLabel("Position: (0, 0)");

        mapDisplayManager.setFocusable(true);
        mapDisplayManager.setBackground(Color.LIGHT_GRAY);

        frame.setLayout(new BorderLayout());
        frame.add(positionLabel, BorderLayout.NORTH);
        frame.add(mapDisplayManager, BorderLayout.CENTER);

        mapDisplayManager.addKeyListener(controller.getKeyListener(positionLabel));
        mapDisplayManager.addMouseListener(controller.getMouseClickListener());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
        SwingUtilities.invokeLater(mapDisplayManager::requestFocusInWindow);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                networkManager.sendDisconnectRequest();
                networkManager.shutdown();
            }
        });
    }
}


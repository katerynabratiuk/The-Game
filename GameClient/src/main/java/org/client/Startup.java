package org.client;

import org.client.UI.MapDisplayManager;
import org.client.UI.UIProvider;
import org.client.game_logic.PayloadRouter;
import org.client.network.PacketsSenderService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Startup {
    private static JFrame frame;
    private static MapDisplayManager mapDisplayManager;
    private static PacketsSenderService networkManager;
    private static PayloadRouter controller;

    public static void launch() {
        frame = new JFrame("The Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        UIProvider.displayMenu(frame);
        frame.setVisible(true);
    }

    public static void startGame(String username) {
        try {
            initSocket();
            UIProvider.displayGame(frame, mapDisplayManager);
            attachControls();
            networkManager.sendJoinRequest();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Failed to start game: " + e.getMessage());
        }
    }

    private static void initSocket() throws IOException {
        mapDisplayManager = new MapDisplayManager();
        controller = new PayloadRouter(mapDisplayManager);
        new Thread(controller).start();

        networkManager = new PacketsSenderService(controller);
        controller.setNetworkManager(networkManager);
        networkManager.start();
    }

    private static void attachControls() {
        JLabel positionLabel = new JLabel("Position: (0, 0)");
        frame.add(positionLabel, BorderLayout.NORTH);

        mapDisplayManager.addKeyListener(controller.getKeyListener(positionLabel));
        mapDisplayManager.addMouseListener(controller.getMouseClickListener());

        SwingUtilities.invokeLater(mapDisplayManager::requestFocusInWindow);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                networkManager.sendDisconnectRequest();
                networkManager.shutdown();
            }
        });
    }

    public static PacketsSenderService getNetworkManager() {
        return networkManager;
    }

    public static JFrame getMainFrame() {
        return frame;
    }
}

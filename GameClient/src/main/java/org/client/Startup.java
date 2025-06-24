package org.client;

import lombok.Getter;
import org.client.UI.MapPanel;
import org.client.UI.UIProvider;
import org.client.game_logic.PayloadRouter;
import org.client.network.PacketsSenderService;
import org.client.network.UDPSocketThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Startup {
    private static JFrame frame;
    private static MapPanel mapDisplayManager;
    private static PayloadRouter controller;

    @Getter
    private static PacketsSenderService packetsSenderService;


    public static void launch() {
        try {
            initComponents();
        } catch (IOException e) {
            throw new RuntimeException(e); // change logs
        }

        frame = new JFrame("The Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        UIProvider.displayMenu(frame);
        frame.setVisible(true);
    }

    public static void startGame() {
        UIProvider.displayGame(frame, mapDisplayManager);
        attachControls();
        packetsSenderService.sendJoinRequest();
    }

    private static void initComponents() throws IOException {
        // TODO: consider refactoring to resolve circular dependencies
        mapDisplayManager = new MapPanel();
        controller = new PayloadRouter(mapDisplayManager);

        var clientThread = new UDPSocketThread(controller);
        packetsSenderService = new PacketsSenderService(clientThread);
        controller.setPacketsSenderService(packetsSenderService);

        new Thread(controller).start();
        clientThread.start();
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
                packetsSenderService.sendDisconnectRequest();
                packetsSenderService.shutdown();
            }
        });
    }

    public static JFrame getMainFrame() {
        return frame;
    }
}

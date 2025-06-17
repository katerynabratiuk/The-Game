package org.client.UI;

import lombok.SneakyThrows;
import org.client.game_logic.ClientController;
import org.client.network.UDPThreadWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class UIProvider {
    private static ActorPanel actorPanel;
    private static UDPThreadWrapper networkManager;
    private static ClientController controller;

    public static void createAndShowGUI() {
        initSocket();
        createGUI();
    }

    @SneakyThrows
    private static void initSocket() {
        actorPanel = new ActorPanel(); // ActorPanel must be ready before passing it to the controller
        controller = new ClientController(actorPanel);
        networkManager = new UDPThreadWrapper(controller);
        controller.setNetworkManager(networkManager);
        networkManager.start();
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
        actorPanel.requestFocusInWindow();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                networkManager.shutdown();
            }
        });
    }
}


//    private static Pair<JFrame, JLabel> createGUI() {
//        JFrame frame = new JFrame("Demo UDP Client");
//        JLabel positionLabel = new JLabel("Position: (0, 0)");
//
//        actorPanel = new ActorPanel();
//        actorPanel.setFocusable(true);
//        actorPanel.setBackground(Color.LIGHT_GRAY);
//
//        frame.setLayout(new BorderLayout());
//        frame.add(positionLabel, BorderLayout.NORTH);
//        frame.add(actorPanel, BorderLayout.CENTER);
//
//        actorPanel.addKeyListener(controller.getKeyListener(positionLabel));
//        actorPanel.requestFocusInWindow();
//
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 300);
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//
//        frame.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent windowEvent) {
//                networkManager.shutdown();
//            }
//        });
//
//        return new Pair<>(frame, positionLabel);
//    }






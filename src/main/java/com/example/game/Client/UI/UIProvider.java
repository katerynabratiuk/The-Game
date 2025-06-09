package com.example.game.Client.UI;

import com.example.game.Client.ActorPositionListenerThread;
import com.example.game.Client.ActorPositionUpdateThread;
import com.example.game.DataStructures.Coordinates;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class UIProvider {
    private static volatile Coordinates position = new Coordinates(0, 0);
    private static ActorPositionUpdateThread senderThread;
    private static ActorPositionListenerThread listenerThread;
    private static ActorPanel actorPanel;

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Demo UDP Client");
        JLabel positionLabel = new JLabel("Position: (0, 0)");

        actorPanel = new ActorPanel();
        actorPanel.setFocusable(true);
        actorPanel.setBackground(Color.LIGHT_GRAY);

        frame.setLayout(new BorderLayout());
        frame.add(positionLabel, BorderLayout.NORTH);
        frame.add(actorPanel, BorderLayout.CENTER);

        try {
            senderThread = new ActorPositionUpdateThread();
            senderThread.start();
            listenerThread = new ActorPositionListenerThread(senderThread.getSocket(), actorPanel, actorPanel.getGameState());
            listenerThread.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to start sender thread: " + e.getMessage());
            return;
        }

        actorPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                position = calculateNewPosition(e.getKeyCode());
                senderThread.updateCoordinates(position);
                positionLabel.setText("Position: (" + position.x() + ", " + position.y() + ")");
            }
        });
        actorPanel.requestFocusInWindow();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (senderThread != null) {
                    senderThread.shutdown();
                }
            }
        });
    }

    // TODO: refactor after java 8 issue solving
    private static Coordinates calculateNewPosition(int keyCode) {
        System.out.println("Pressed key: " + keyCode);
        if (keyCode == KeyEvent.VK_LEFT) {
            return new Coordinates(position.x() - 1, position.y());
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            return new Coordinates(position.x() + 1, position.y());
        } else if (keyCode == KeyEvent.VK_UP) {
            return new Coordinates(position.x(), position.y() - 1);
        } else if (keyCode == KeyEvent.VK_DOWN) {
            return new Coordinates(position.x(), position.y() + 1);
        } else {
            return position;
        }
    }
}

package org.client.UI;

import org.client.ActorPositionListenerThread;
import org.client.InputsSenderThread;
import org.lib.DataStructures.Coordinates;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class UIProvider {
    private static volatile Coordinates position = new Coordinates(0, 0);
    private static InputsSenderThread senderThread;
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
            senderThread = new InputsSenderThread();
            senderThread.start();
            listenerThread = new ActorPositionListenerThread(senderThread.getSocket(), actorPanel);
            listenerThread.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to start sender thread: " + e.getMessage());
            return;
        }

        actorPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                senderThread.updateInputsQueue(e.getKeyCode());
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
}

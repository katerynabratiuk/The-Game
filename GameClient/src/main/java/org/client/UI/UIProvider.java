package org.client.UI;

import javax.swing.*;

public class UIProvider {
    public static void displayMenu(JFrame frame) {
        frame.setContentPane(new MenuPanel(frame));
        refresh(frame);
    }

    public static void displayRegister(JFrame frame) {
        frame.setContentPane(new RegisterPanel(frame));
        refresh(frame);
    }

    public static void displayLogin(JFrame frame) {
        frame.setContentPane(new LoginPanel(frame));
        refresh(frame);
    }

    public static void displayGame(JFrame frame, MapPanel gamePanel) {
        frame.getContentPane().removeAll();
        frame.setLayout(new java.awt.BorderLayout());
        frame.add(new JLabel("Position: (0, 0)"), java.awt.BorderLayout.NORTH);
        frame.add(gamePanel, java.awt.BorderLayout.CENTER);
        refresh(frame);
        SwingUtilities.invokeLater(gamePanel::requestFocusInWindow);
    }

    private static void refresh(JFrame frame) {
        frame.revalidate();
        frame.repaint();
    }
}

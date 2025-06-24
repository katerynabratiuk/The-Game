package org.client.UI;

import javax.swing.*;
import java.awt.*;

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

        JPanel rankPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        frame.add(rankPanel, BorderLayout.NORTH);
        frame.add(gamePanel, BorderLayout.CENTER);

        refresh(frame);
        SwingUtilities.invokeLater(gamePanel::requestFocusInWindow);
    }

    private static void refresh(JFrame frame) {
        frame.revalidate();
        frame.repaint();
    }
}

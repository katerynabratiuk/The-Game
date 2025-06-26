package org.client.UI;

import org.client.game_logic.PayloadRouter;
import org.client.network.PacketsSenderService;
import org.lib.data.dto.CharacterDTO;
import org.lib.data.dto.ItemDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

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

    public static void displayCharacterSelection(JFrame frame, ArrayList<CharacterDTO> characters) {
        frame.setContentPane(new CharacterSelectPanel(frame, characters));
        refresh(frame);
    }


    public static void displayWeaponSelection(JFrame frame, ArrayList<ItemDTO> items) {
        frame.setContentPane(new WeaponSelectPanel(frame, items));
        refresh(frame);
    }

    public static void displayPowerUpSelection(JFrame frame, ArrayList<ItemDTO> items) {
        frame.setContentPane(new PowerUpSelectPanel(frame, items));
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

    public static void attachControls(JFrame frame, MapPanel mapPanel, PayloadRouter controller, PacketsSenderService service) {
        mapPanel.enableInput();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                service.sendDisconnectRequest();
                service.shutdown();
            }
        });
    }

    public static void detachControls(JFrame frame) {
        for (WindowListener listener : frame.getWindowListeners()) {
            frame.removeWindowListener(listener);
        }
    }


}

package org.client.UI;

import org.lib.data_structures.payloads.game.Notification;

import javax.swing.*;
import java.awt.*;

public class PopupRenderer {
    public static void renderNotification(Notification notif, MapPanel mapPanel) {
        SwingUtilities.invokeLater(() -> {
            JDialog notificationDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(mapPanel), "Notification", false);
            notificationDialog.setLayout(new BorderLayout());

            JLabel messageLabel = new JLabel(notif.getMessage());
            messageLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JButton closeBtn = new JButton("Ok");
            closeBtn.addActionListener(e -> notificationDialog.dispose());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(closeBtn);

            notificationDialog.add(messageLabel, BorderLayout.CENTER);
            notificationDialog.add(buttonPanel, BorderLayout.SOUTH);

            notificationDialog.setSize(300, 150);
            notificationDialog.setLocationRelativeTo(mapPanel);
            notificationDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            notificationDialog.setResizable(false);

            notificationDialog.setVisible(true);
        });
    }
}

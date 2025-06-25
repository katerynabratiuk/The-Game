package org.client.UI;

import javax.swing.*;
import java.awt.*;

public class WeaponSelectPanel extends JPanel {
    private String selectedWeapon = null;

    public WeaponSelectPanel(JFrame frame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Choose Your Weapon");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        String[] names = {"MachineGun", "Rocket", "Pistol"};
        String[] imagePaths = {
                "/images/machine-gun.png",
                "/images/rocket.png",
                "/images/pistol.png"
        };

        for (int i = 0; i < names.length; i++) {
            java.net.URL imageURL = getClass().getResource(imagePaths[i]);

            JButton weaponButton;
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                weaponButton = new JButton(names[i], scaledIcon);
                weaponButton.setHorizontalTextPosition(SwingConstants.CENTER);
                weaponButton.setVerticalTextPosition(SwingConstants.BOTTOM);
            } else {
                weaponButton = new JButton(names[i] + " (no image)");
            }

            weaponButton.setPreferredSize(new Dimension(150, 150));
            final int index = i;
            weaponButton.addActionListener(e -> selectedWeapon = names[index]);

            gbc.gridx = i;
            add(weaponButton, gbc);
        }

        gbc.gridy++;
        gbc.gridx = 1;
        JButton continueBtn = new JButton("Select and Continue");
        add(continueBtn, gbc);

        continueBtn.addActionListener(e -> {
            if (selectedWeapon != null) {
                System.out.println("Selected weapon: " + selectedWeapon);
                // TODO: save weapon
                UIProvider.displayItemSelection(frame);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a weapon.");
            }
        });
    }
}

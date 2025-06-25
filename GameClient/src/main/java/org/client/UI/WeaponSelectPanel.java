package org.client.UI;

import org.client.Startup;
import org.lib.data_structures.dto.ItemDTO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class WeaponSelectPanel extends JPanel {
    private ItemDTO selectedWeapon = null;

    public WeaponSelectPanel(JFrame frame, List<ItemDTO> weapons) {
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

        for (int i = 0; i < weapons.size(); i++) {
            ItemDTO weapon = weapons.get(i);

            String name = weapon.getName();
            String imagePath = weapon.getImagePath(); // наприклад: "/images/machine-gun.png"

            java.net.URL imageURL = getClass().getResource(imagePath);

            JButton weaponButton;
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                weaponButton = new JButton(name, scaledIcon);
                weaponButton.setHorizontalTextPosition(SwingConstants.CENTER);
                weaponButton.setVerticalTextPosition(SwingConstants.BOTTOM);
            } else {
                weaponButton = new JButton(name + " (no image)");
            }

            weaponButton.setPreferredSize(new Dimension(150, 150));
            weaponButton.addActionListener(e -> selectedWeapon = weapon);

            gbc.gridx = i;
            add(weaponButton, gbc);
        }

        gbc.gridy++;
        gbc.gridx = 1;
        JButton continueBtn = new JButton("Select and Continue");
        add(continueBtn, gbc);

        continueBtn.addActionListener(e -> {
            if (selectedWeapon != null) {
                System.out.println("Selected weapon: " + selectedWeapon.getName());
                Startup.getUserPick().setWeaponId(selectedWeapon.getId());
                Startup.getPacketsSenderService().sendPowerUpRequest();
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a weapon.");
            }
        });
    }
}

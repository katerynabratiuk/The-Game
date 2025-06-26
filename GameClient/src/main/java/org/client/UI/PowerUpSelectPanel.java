package org.client.UI;

import org.client.Startup;
import org.lib.data_structures.dto.ItemDTO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PowerUpSelectPanel extends JPanel {
    private ItemDTO selectedItem = null;

    public PowerUpSelectPanel(JFrame frame, List<ItemDTO> powerUps) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Choose Your Power-Up");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        for (int i = 0; i < powerUps.size(); i++) {
            ItemDTO item = powerUps.get(i);
            String name = item.getName();
            String imagePath = item.getImagePath();

            java.net.URL imageURL = getClass().getResource(imagePath);

            JButton itemButton;
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                itemButton = new JButton(name, scaledIcon);
                itemButton.setHorizontalTextPosition(SwingConstants.CENTER);
                itemButton.setVerticalTextPosition(SwingConstants.BOTTOM);
            } else {
                itemButton = new JButton(name + " (no image)");
            }

            itemButton.setPreferredSize(new Dimension(150, 150));
            final int index = i;
            itemButton.addActionListener(e -> selectedItem = powerUps.get(index));

            gbc.gridx = i;
            add(itemButton, gbc);
        }

        gbc.gridy++;
        gbc.gridx = 1;
        JButton continueBtn = new JButton("Select and Continue");
        add(continueBtn, gbc);

        continueBtn.addActionListener(e -> {
            if (selectedItem != null) {
                System.out.println("Selected power-up: " + selectedItem.getName());
                Startup.getUserPick().setPowerUpId(selectedItem.getId());

                Startup.getPacketsSenderService().sendUserPickAndJoin(
                        Startup.getUserPick().getCharacterId(),
                        Startup.getUserPick().getWeaponId(),
                        Startup.getUserPick().getPowerUpId()
                );

                Startup.startGame();
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a power-up.");
            }
        });
    }
}

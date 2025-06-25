package org.client.UI;

import org.client.Startup;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class CharacterSelectPanel extends JPanel {
    private String selectedCharacter = null;

    public CharacterSelectPanel(JFrame frame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Choose Your Character");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;


        String[] names = {"Ninja", "Tank", "Alien"};
        String[] imagePaths = {
                "/images/ninja.png",
                "/images/tank.png",
                "/images/alien.png"
        };

        for (int i = 0; i < names.length; i++) {
            java.net.URL imageURL = getClass().getResource(imagePaths[i]);

            JButton charButton;
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                charButton = new JButton(names[i], scaledIcon);
                charButton.setHorizontalTextPosition(SwingConstants.CENTER);
                charButton.setVerticalTextPosition(SwingConstants.BOTTOM);
            } else {
                charButton = new JButton(names[i] + " (no image)");
            }

            charButton.setPreferredSize(new Dimension(150, 150));
            final int index = i;
            charButton.addActionListener(e -> selectedCharacter = names[index]);

            gbc.gridx = i;
            add(charButton, gbc);
        }


        gbc.gridy++;
        gbc.gridx = 1;
        JButton continueBtn = new JButton("Select and Continue");
        add(continueBtn, gbc);

        continueBtn.addActionListener(e -> {
            if (selectedCharacter != null) {
                System.out.println("Selected character: " + selectedCharacter);
                // TODO: save character
                UIProvider.displayWeaponSelection(frame);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a character.");
            }
        });

    }
}

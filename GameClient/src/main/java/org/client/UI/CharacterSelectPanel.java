package org.client.UI;

import org.client.Startup;
import org.lib.data_structures.payloads.queries.UserPickPayload;
import org.server.db.dto.CharacterDTO;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CharacterSelectPanel extends JPanel {
    private CharacterDTO selectedCharacter = null;

    public CharacterSelectPanel(JFrame frame, ArrayList<CharacterDTO> characters) {
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


        for (int i = 0; i < characters.size(); i++) {
            CharacterDTO c = characters.get(i);
            String name = c.getName();
            String description = c.getDescription();
            String imagePath = c.getImagePath();

            java.net.URL imageURL = getClass().getResource(imagePath);

            JButton charButton;
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                charButton = new JButton(name, scaledIcon);
                charButton.setHorizontalTextPosition(SwingConstants.CENTER);
                charButton.setVerticalTextPosition(SwingConstants.BOTTOM);
            } else {
                charButton = new JButton(name + " (no image)");
            }

            charButton.setPreferredSize(new Dimension(150, 150));
            charButton.setToolTipText(description); // можна навести — і показується опис
            charButton.addActionListener(e -> selectedCharacter = c);

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
                Startup.getUserPick().setCharacterId(selectedCharacter.getId());
                Startup.getPacketsSenderService().sendWeaponListRequest();
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a character.");
            }
        });

    }
}

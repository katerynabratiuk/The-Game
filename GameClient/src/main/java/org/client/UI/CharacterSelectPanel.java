package org.client.UI;

import org.client.Startup;
import org.lib.data_structures.dto.CharacterDTO;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CharacterSelectPanel extends JPanel {
    private CharacterDTO selectedCharacter = null;
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final JPanel charactersPanel = new JPanel(new FlowLayout());

    public CharacterSelectPanel(JFrame frame, ArrayList<CharacterDTO> characters) {
        setLayout(new GridBagLayout());
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // title
        JLabel title = new JLabel("Choose Your Character");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);

        // search bar
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JTextField searchField = new JTextField(20);
        add(searchField, gbc);

        JButton searchBtn = new JButton("Search");
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        add(searchBtn, gbc);

        // filter panel (radio buttons)
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JRadioButton fastRadio = new JRadioButton("Fast");
        JRadioButton armorRadio = new JRadioButton("Armor");
        filterPanel.add(fastRadio);
        filterPanel.add(armorRadio);
        add(filterPanel, gbc);

        // characters panel
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        charactersPanel.setPreferredSize(new Dimension(500, 200));
        add(charactersPanel, gbc);

        // continue button
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
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

        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            boolean isFast = fastRadio.isSelected();
            boolean hasArmor = armorRadio.isSelected();

            System.out.println("Search query: " + query);
            System.out.println("Filters — Fast: " + isFast + ", Armor: " + hasArmor);

            Startup.getPacketsSenderService().sendCharacterFilterRequest(query, isFast, hasArmor);

        });

        displayCharacters(characters);
    }

    private void displayCharacters(ArrayList<CharacterDTO> characters) {
        charactersPanel.removeAll();
        for (CharacterDTO c : characters) {
            String name = c.getName();
            String description = c.getDescription();
            String imagePath = c.getImagePath();

            var imageURL = getClass().getResource(imagePath);

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
            charButton.setToolTipText(description);
            charButton.addActionListener(e -> selectedCharacter = c);

            charactersPanel.add(charButton);
        }
        charactersPanel.revalidate();
        charactersPanel.repaint();
    }
}

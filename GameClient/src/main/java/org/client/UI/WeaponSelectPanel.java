package org.client.UI;

import org.lib.data.dto.ItemDTO;
import org.client.GameContext;
import org.lib.data.payloads.queries.search.WeaponFilterPayload;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class WeaponSelectPanel extends JPanel {
    private ItemDTO selectedWeapon = null;
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final JPanel weaponsPanel = new JPanel(new FlowLayout());

    public WeaponSelectPanel(JFrame frame, List<ItemDTO> weapons) {
        setLayout(new GridBagLayout());
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("Choose Your Weapon");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);

        // Search bar
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JTextField searchField = new JTextField(20);
        add(searchField, gbc);

        JButton searchBtn = new JButton("Search");
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        add(searchBtn, gbc);

        // Sort checkboxes
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JCheckBox damageCheck = new JCheckBox("Sort by Damage");
        JCheckBox sprayCheck = new JCheckBox("Sort by Spray");
        JCheckBox rateCheck = new JCheckBox("Sort by Rate of Fire");
        sortPanel.add(damageCheck);
        sortPanel.add(sprayCheck);
        sortPanel.add(rateCheck);
        add(sortPanel, gbc);

        // Weapons panel
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        weaponsPanel.setPreferredSize(new Dimension(600, 200));
        add(weaponsPanel, gbc);

        // Continue button
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        JButton continueBtn = new JButton("Select and Continue");
        add(continueBtn, gbc);

        continueBtn.addActionListener(e -> {
            if (selectedWeapon != null) {
                System.out.println("Selected weapon: " + selectedWeapon.getName());
                GameContext.getUserPick().setWeaponId(selectedWeapon.getId());
                GameContext.getPacketsSenderService().sendPowerUpRequest();
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a weapon.");
            }
        });

        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();

            List<WeaponFilterPayload.SortField> sortFields = new ArrayList<>();
            if (damageCheck.isSelected()) sortFields.add(WeaponFilterPayload.SortField.DAMAGE);
            if (sprayCheck.isSelected()) sortFields.add(WeaponFilterPayload.SortField.SPRAY);
            if (rateCheck.isSelected()) sortFields.add(WeaponFilterPayload.SortField.RATE_OF_FIRE);

            System.out.println("Weapon query: " + query);
            System.out.println("Sort fields: " + sortFields);

            GameContext.getPacketsSenderService().sendWeaponFilterRequest(query, sortFields);
        });

        displayWeapons(weapons);
    }

    private void displayWeapons(List<ItemDTO> weapons) {
        weaponsPanel.removeAll();
        for (ItemDTO w : weapons) {
            String name = w.getName();
            String imagePath = w.getImagePath();

            var imageURL = getClass().getResource(imagePath);

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
            weaponButton.addActionListener(e -> selectedWeapon = w);

            weaponsPanel.add(weaponButton);
        }
        weaponsPanel.revalidate();
        weaponsPanel.repaint();
//
//        gbc.gridy++;
//        gbc.gridx = 1;
//        JButton continueBtn = new JButton("Select and Continue");
//        add(continueBtn, gbc);
//
//        continueBtn.addActionListener(e -> {
//            if (selectedWeapon != null) {
//                System.out.println("Selected weapon: " + selectedWeapon.getName());
//                GameContext.getUserPick().setWeaponId(selectedWeapon.getId());
//                GameContext.getPacketsSenderService().sendPowerUpRequest();
//            } else {
//               // JOptionPane.showMessageDialog("Please select a weapon.");
//            }
//        });
    }
}

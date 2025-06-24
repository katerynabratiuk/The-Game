package org.client.UI;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {

    public RegisterPanel(JFrame frame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel title = new JLabel("Register");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 0;
        add(title, gbc);

        gbc.gridy++;
        add(new JLabel("Username:"), gbc);

        JTextField usernameField = new JTextField(15);
        gbc.gridy++;
        add(usernameField, gbc);

        gbc.gridy++;
        add(new JLabel("Password:"), gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridy++;
        add(passwordField, gbc);

        gbc.gridy++;
        add(new JLabel("Confirm Password:"), gbc);

        JPasswordField confirmPasswordField = new JPasswordField(15);
        gbc.gridy++;
        add(confirmPasswordField, gbc);

        JButton registerBtn = new JButton("Register");
        gbc.gridy++;
        add(registerBtn, gbc);

        JButton backBtn = new JButton("Back");
        gbc.gridy++;
        add(backBtn, gbc);

        registerBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
            } else if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match.");
            } else {
                // TODO: registration logic

                JOptionPane.showMessageDialog(frame, "Registered successfully as: " + username);
                UIProvider.startGame(frame, username);
            }
        });

        backBtn.addActionListener(e -> UIProvider.createAndShowGUI());
    }
}

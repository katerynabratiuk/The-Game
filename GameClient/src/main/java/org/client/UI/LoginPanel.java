package org.client.UI;

import org.client.Startup;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    public LoginPanel(JFrame frame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel title = new JLabel("Login to The Game");
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

        JButton loginBtn = new JButton("Login");
        gbc.gridy++;
        add(loginBtn, gbc);

        JLabel notRegisteredLabel = new JLabel("Not registered yet?");
        gbc.gridy++;
        add(notRegisteredLabel, gbc);

        JButton registerBtn = new JButton("Register");
        gbc.gridy++;
        add(registerBtn, gbc);

        registerBtn.addActionListener(e -> {
            UIProvider.displayRegister(frame);
        });


        add(registerBtn, gbc);
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (!username.isEmpty() && !password.isEmpty()) {
                try {
                    Startup.getPacketsSenderService().sendLogin(username, password);
                    Startup.getPacketsSenderService().sendCharacterListRequest();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Login failed: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter both username and password!");
            }
        });

    }
}

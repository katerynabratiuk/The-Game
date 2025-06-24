package org.client.UI;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {

    public MenuPanel(JFrame frame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel title = new JLabel("The Game");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridy = 0;
        add(title, gbc);

        gbc.gridy++;
        JButton loginBtn = new JButton("Login");
        add(loginBtn, gbc);

        loginBtn.addActionListener(e -> {
            UIProvider.displayLogin(frame);
        });

        gbc.gridy++;
        add(new JLabel("Not registered yet?"), gbc);

        gbc.gridy++;
        JButton registerBtn = new JButton("Register");
        add(registerBtn, gbc);

        registerBtn.addActionListener(e -> {
            UIProvider.displayRegister(frame);
        });


    }
}

package org.client;

import javax.swing.*;


public class GameClient {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameContext::launch);
    }
}
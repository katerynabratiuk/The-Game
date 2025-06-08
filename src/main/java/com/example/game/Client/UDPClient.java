package com.example.game.Client;
import com.example.game.Client.UI.UIProvider;
import javax.swing.*;


public class UDPClient {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(UIProvider::createAndShowGUI);
    }
}
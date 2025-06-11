package org.client;
import org.client.UI.UIProvider;
import javax.swing.*;


public class UDPClient {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(UIProvider::createAndShowGUI);
    }
}
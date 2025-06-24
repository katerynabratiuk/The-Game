package org.client;
import lombok.SneakyThrows;

import javax.swing.*;


public class UDPClient {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Startup::launch);
    }
}
package org.client;

import lombok.Getter;
import org.client.UI.MapPanel;
import org.client.UI.UIProvider;
import org.client.game_logic.PayloadRouter;
import org.client.network.PacketsSenderService;
import org.client.network.UDPSocketThread;
import org.lib.data_structures.payloads.queries.UserPickPayload;

import javax.swing.*;
import java.io.IOException;

public class Startup {
    private static JFrame frame;
    private static MapPanel mapPanel;
    private static PayloadRouter controller;

    @Getter
    private static PacketsSenderService packetsSenderService;

    @Getter
    private static final UserPickPayload userPick = new UserPickPayload();


    public static void launch() {
        try {
            initComponents();
        } catch (IOException e) {
            throw new RuntimeException(e); // change logs
        }

        frame = new JFrame("The Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        UIProvider.displayMenu(frame);
        frame.setVisible(true);
    }

    public static void startGame() {
        UIProvider.displayGame(frame, mapPanel);
        UIProvider.attachControls(frame, mapPanel, controller, packetsSenderService);
        packetsSenderService.sendJoinRequest();
    }

    private static void initComponents() throws IOException {
        // TODO: consider refactoring to resolve circular dependencies
        mapPanel = new MapPanel();
        controller = new PayloadRouter(mapPanel);

        var clientThread = new UDPSocketThread(controller);
        packetsSenderService = new PacketsSenderService(clientThread);
        controller.setPacketsSenderService(packetsSenderService);

        new Thread(controller).start();
        clientThread.start();
    }

    public static JFrame getMainFrame() {
        return frame;
    }
}

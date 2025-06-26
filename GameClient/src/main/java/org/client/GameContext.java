package org.client;

import lombok.Getter;
import org.client.UI.MapPanel;
import org.client.UI.UIProvider;
import org.client.game_logic.InputHandler;
import org.client.game_logic.PayloadRouter;
import org.client.network.PacketsSenderService;
import org.client.network.UDPSocketThread;
import org.lib.data.payloads.queries.UserPickPayload;

import javax.swing.*;
import java.io.IOException;


public class GameContext {
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;

    @Getter private static JFrame frame;
    @Getter private static final UserPickPayload userPick = new UserPickPayload();
    @Getter private static PacketsSenderService packetsSenderService;

    private static MapPanel mapPanel;
    private static PayloadRouter controller;

    public static void launch() {
        try {
            initComponents();
            setupMainWindow();
            UIProvider.displayMenu(frame);
            frame.setVisible(true);
        } catch (IOException e) {
            logAndThrowStartupError(e);
        }
    }

    public static void startGame() {
        UIProvider.displayGame(frame, mapPanel);
        UIProvider.attachControls(frame, mapPanel, controller, packetsSenderService);
        // send join() request on demand
    }

    private static void initComponents() throws IOException {
        mapPanel = new MapPanel();
        controller = new PayloadRouter(mapPanel);

        var udpSocketThread = new UDPSocketThread(controller);
        packetsSenderService = new PacketsSenderService(udpSocketThread);
        new InputHandler(mapPanel, packetsSenderService);

        startThreads(controller, udpSocketThread);
    }

    private static void setupMainWindow() {
        frame = new JFrame("The Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLocationRelativeTo(null);
    }

    private static void startThreads(Runnable controller, Thread udpSocketThread) {
        new Thread(controller).start();
        udpSocketThread.start();
    }

    private static void logAndThrowStartupError(IOException e) {
        System.err.println("Failed to launch the game: " + e.getMessage());
        throw new RuntimeException("Startup failed", e);
    }

    public static JFrame getMainFrame() {
        return frame;
    }
}

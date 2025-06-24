package org.client.network;

import lombok.Getter;
import org.client.game_logic.PayloadRouter;
import org.lib.packet_processing.receive.Decoder;
import org.lib.packet_processing.receive.Decryptor;
import org.lib.packet_processing.receive.PacketReceiverThread;
import org.lib.packet_processing.registry.SocketAddressRegistry;
import org.lib.packet_processing.send.Encoder;
import org.lib.packet_processing.send.Encryptor;
import org.lib.packet_processing.send.UnicastThread;

import java.io.IOException;
import java.net.*;
import java.util.*;

import static org.lib.environment.EnvLoader.ENV_VARS;

public class UDPSocketThread extends Thread {
    @Getter private volatile UnicastThread senderThread;
    @Getter private volatile PacketReceiverThread receiverThread;
    @Getter private final String clientId = UUID.randomUUID().toString();
    @Getter private final PayloadRouter controller;
    private final int RECONNECT_DELAY_MS = 10_000;
    private volatile boolean running = true;

    public UDPSocketThread(PayloadRouter controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        while (running) {
            try {
                initThreads();
                handleConnectionLoss();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (IOException e) {
                handleNetworkError(e);
            }
        }
    }

    public void shutdown() {
        running = false;
        if (senderThread != null) senderThread.interrupt();
        if (receiverThread != null) receiverThread.interrupt();
    }

    private void initThreads() throws SocketException, UnknownHostException, InterruptedException {
        System.out.println("Creating a client socket...");
        var socket = new DatagramSocket();
        var registry = getServerAddressRegistry();

        senderThread = new UnicastThread(socket, new Encoder(), new Encryptor(), registry);
        receiverThread = new PacketReceiverThread(socket, controller, new Decoder(), new Decryptor(), null);

        senderThread.start();
        receiverThread.start();

        senderThread.join();
        receiverThread.join();
    }

    private InetSocketAddress getServerAddress() throws UnknownHostException {
        return new InetSocketAddress(
                InetAddress.getByName(ENV_VARS.get("UDP_SERVER_HOST")),
                Integer.parseInt(ENV_VARS.get("UDP_SERVER_PORT"))
        );
    }

    private SocketAddressRegistry getServerAddressRegistry() throws UnknownHostException {
        var serverAddress = getServerAddress();
        var registry = new SocketAddressRegistry();
        registry.add(getClientId(), serverAddress);
        return registry;
    }

    private void handleConnectionLoss() throws InterruptedException {
        System.err.println("Connection lost. Reconnecting...");
        Thread.sleep(RECONNECT_DELAY_MS);
    }

    // check if sleep() needed
    private void handleNetworkError(IOException e) {
        System.err.println("Network error: " + e.getMessage());
        if (running) {
            System.err.println("Attempting reconnection in " + RECONNECT_DELAY_MS/1000 + " seconds...");
        }
    }
}

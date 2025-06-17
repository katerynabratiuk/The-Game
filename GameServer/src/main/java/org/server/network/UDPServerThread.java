package org.server.network;

import org.lib.PacketProcessing.receive.Decoder;
import org.lib.PacketProcessing.receive.Decryptor;
import org.lib.PacketProcessing.receive.PacketReceiverThread;
import org.lib.PacketProcessing.send.Encoder;
import org.lib.PacketProcessing.send.Encryptor;
import org.lib.PacketProcessing.send.PacketSenderThread;
import org.server.gameLogic.PlayerController;

import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap; // can we use this?

import static org.lib.Environment.EnvLoader.ENV_VARS;

public class UDPServerThread extends Thread {
    public DatagramSocket socket;
    private final int PORT = Integer.parseInt(ENV_VARS.get("UDP_SERVER_PORT"));
    private final String SERVER_ADDRESS = ENV_VARS.get("UDP_SERVER_HOST");
    private final PacketReceiverThread  receivingThread;
    private final PacketSenderThread sendingThread;

    public UDPServerThread() throws SocketException {
        this.socket = new DatagramSocket(PORT);
        Set<SocketAddress> clients = ConcurrentHashMap.newKeySet();
        this.sendingThread = new PacketSenderThread(socket, new Encoder(), new Encryptor(), clients);
        this.receivingThread = new PacketReceiverThread(socket, new PlayerController(), new Decoder(), new Decryptor(), clients);
    }

    public void run() {
        try {
            connectSocket();
            startProcessingThreads();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        socket.close();
    }

    private void startProcessingThreads() {
        sendingThread.start();
        receivingThread.start();
    }

    private void connectSocket() throws UnknownHostException {
        try {
            InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);
            socket = new DatagramSocket(PORT, serverAddress);
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Server listening on port " + PORT + "...");
    }
}

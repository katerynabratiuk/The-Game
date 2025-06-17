package org.server.network;

import lombok.SneakyThrows;
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
    private final PacketReceiverThread  receivingThread;
    private final PacketSenderThread sendingThread;

    @SneakyThrows
    public UDPServerThread(PlayerController controller) {
        String SERVER_ADDRESS = ENV_VARS.get("UDP_SERVER_HOST");
        InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);
        int PORT = Integer.parseInt(ENV_VARS.get("UDP_SERVER_PORT"));
        this.socket = new DatagramSocket(PORT, serverAddress);
        Set<SocketAddress> receivers = ConcurrentHashMap.newKeySet();


        this.sendingThread = new PacketSenderThread(socket, new Encoder(), new Encryptor(), receivers);
        this.receivingThread = new PacketReceiverThread(socket, controller, new Decoder(), new Decryptor(), receivers);

        System.out.println("Server running on port " + PORT + "..."); // TODO: move log
    }

    public void run() {
        startProcessingThreads();
    }

    private void startProcessingThreads() {
        sendingThread.start();
        receivingThread.start();
    }
}

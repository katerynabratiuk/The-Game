package org.server.network;

import lombok.Getter;
import lombok.SneakyThrows;
import org.lib.packet_processing.receive.Decoder;
import org.lib.packet_processing.receive.Decryptor;
import org.lib.packet_processing.receive.PacketReceiverThread;
import org.lib.packet_processing.registry.SocketAddressRegistry;
import org.lib.packet_processing.send.*;
import org.lib.packet_processing.send.strategies.BroadcastStrategy;
import org.lib.packet_processing.send.strategies.DynamicRegistryStrategy;
import org.lib.packet_processing.send.strategies.UnicastStrategy;
import org.server.game_logic.PayloadRouter;

import java.net.*;

import static org.lib.environment.EnvLoader.ENV_VARS;

public class UDPSocketThread extends Thread {
    @Getter public DatagramSocket socket;
    @Getter private final PacketReceiverThread  receivingThread;
    @Getter private final SenderThread broadcastThread;
    @Getter private final SenderThread unicastThread;

    @SneakyThrows
    public UDPSocketThread(PayloadRouter controller) {
        String SERVER_ADDRESS = ENV_VARS.get("UDP_SERVER_HOST");
        int PORT = Integer.parseInt(ENV_VARS.get("UDP_SERVER_PORT"));
        var serverAddress = InetAddress.getByName(SERVER_ADDRESS);
        this.socket = new DatagramSocket(PORT, serverAddress);
        var registry = new SocketAddressRegistry();

        this.unicastThread = new SenderThread(socket, new Encoder(), new Encryptor(), new UnicastStrategy(registry));
        this.broadcastThread = new SenderThread(socket, new Encoder(), new Encryptor(), new BroadcastStrategy(new DynamicRegistryStrategy(registry)));
        this.receivingThread = new PacketReceiverThread(socket, controller, new Decoder(), new Decryptor(), registry);

        registry.addObserver(broadcastThread);
        System.out.println("Server running on host " + serverAddress + ":" + PORT + "...");
    }

    public void run() {
        startProcessingThreads();
    }

    private void startProcessingThreads() {
        unicastThread.start();
        broadcastThread.start();
        receivingThread.start();
    }
}

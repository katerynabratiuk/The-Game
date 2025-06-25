package org.server.network;

import lombok.Getter;
import lombok.SneakyThrows;
import org.lib.packet_processing.receive.Decoder;
import org.lib.packet_processing.receive.Decryptor;
import org.lib.packet_processing.receive.PacketReceiverThread;
import org.lib.packet_processing.registry.SocketAddressRegistry;
import org.lib.packet_processing.send.Encoder;
import org.lib.packet_processing.send.Encryptor;
import org.lib.packet_processing.send.BroadcastThread;
import org.lib.packet_processing.send.UnicastThread;
import org.lib.packet_processing.strategies.DynamicRegistryStrategy;
import org.server.game_logic.PayloadRouter;

import java.net.*;

import static org.lib.environment.EnvLoader.ENV_VARS;

public class UDPSocketThread extends Thread {
    @Getter public DatagramSocket socket;
    @Getter private final PacketReceiverThread  receivingThread;
    @Getter private final BroadcastThread broadcastThread;
    @Getter private final UnicastThread unicastThread;

    @SneakyThrows
    public UDPSocketThread(PayloadRouter controller) {
        String SERVER_ADDRESS = ENV_VARS.get("UDP_SERVER_HOST");
        int PORT = Integer.parseInt(ENV_VARS.get("UDP_SERVER_PORT"));
        var serverAddress = InetAddress.getByName(SERVER_ADDRESS);
        this.socket = new DatagramSocket(PORT, serverAddress);
        var registry = new SocketAddressRegistry();

        this.unicastThread = new UnicastThread(socket, new Encoder(), new Encryptor(), registry);
        this.broadcastThread = new BroadcastThread(socket, new Encoder(), new Encryptor(), new DynamicRegistryStrategy(registry));
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

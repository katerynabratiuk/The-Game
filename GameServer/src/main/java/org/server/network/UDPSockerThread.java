package org.server.network;

import lombok.Getter;
import lombok.SneakyThrows;
import org.lib.packet_processing.receive.Decoder;
import org.lib.packet_processing.receive.Decryptor;
import org.lib.packet_processing.receive.PacketReceiverThread;
import org.lib.packet_processing.registry.SocketAddressRegistry;
import org.lib.packet_processing.send.Encoder;
import org.lib.packet_processing.send.Encryptor;
import org.lib.packet_processing.send.PacketSenderThread;
import org.lib.packet_processing.strategies.DynamicRegistryStrategy;
import org.server.game_logic.PayloadRouter;

import java.net.*;

import static org.lib.environment.EnvLoader.ENV_VARS;

public class UDPSockerThread extends Thread {
    @Getter public DatagramSocket socket;
    @Getter private final PacketReceiverThread  receivingThread;
    @Getter private final PacketSenderThread sendingThread;

    @SneakyThrows
    public UDPSockerThread(PayloadRouter controller) {
        String SERVER_ADDRESS = ENV_VARS.get("UDP_SERVER_HOST");
        InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);
        int PORT = Integer.parseInt(ENV_VARS.get("UDP_SERVER_PORT"));
        this.socket = new DatagramSocket(PORT, serverAddress);
        var registry = new SocketAddressRegistry();
        this.sendingThread = new PacketSenderThread(socket, new Encoder(), new Encryptor(), new DynamicRegistryStrategy(registry));
        this.receivingThread = new PacketReceiverThread(socket, controller, new Decoder(), new Decryptor(), registry);
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

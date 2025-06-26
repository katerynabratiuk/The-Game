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
    @Getter private final DatagramSocket socket;
    @Getter private final PacketReceiverThread receivingThread;
    @Getter private final SenderThread broadcastThread;
    @Getter private final SenderThread unicastThread;

    private final SocketAddressRegistry registry;

    @SneakyThrows
    public UDPSocketThread(PayloadRouter controller) {
        this.registry = new SocketAddressRegistry();
        this.socket = createServerSocket();
        this.unicastThread = createUnicastSender();
        this.broadcastThread = createBroadcastSender();
        this.receivingThread = createReceiver(controller);

        registry.addObserver(broadcastThread);
        logServerStart();
    }

    @Override
    public void run() {
        startAllThreads();
    }

    private DatagramSocket createServerSocket() throws UnknownHostException, SocketException {
        String host = ENV_VARS.get("UDP_SERVER_HOST");
        int port = Integer.parseInt(ENV_VARS.get("UDP_SERVER_PORT"));
        InetAddress address = InetAddress.getByName(host);
        return new DatagramSocket(port, address);
    }

    private SenderThread createUnicastSender() {
        return new SenderThread(socket, new Encoder(), new Encryptor(), new UnicastStrategy(registry));
    }

    private SenderThread createBroadcastSender() {
        var strategy = new BroadcastStrategy(new DynamicRegistryStrategy(registry));
        return new SenderThread(socket, new Encoder(), new Encryptor(), strategy);
    }

    private PacketReceiverThread createReceiver(PayloadRouter controller) {
        return new PacketReceiverThread(socket, controller, new Decoder(), new Decryptor(), registry);
    }

    private void startAllThreads() {
        unicastThread.start();
        broadcastThread.start();
        receivingThread.start();
    }

    private void logServerStart() {
        var address = socket.getLocalAddress();
        int port = socket.getLocalPort();
        System.out.println("Server running on host " + address + ":" + port + "...");
    }
}

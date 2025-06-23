package org.client.network;

import lombok.Getter;
import org.client.game_logic.PayloadRouter;
import org.lib.packet_processing.receive.Decoder;
import org.lib.packet_processing.receive.Decryptor;
import org.lib.packet_processing.receive.PacketReceiverThread;
import org.lib.packet_processing.send.Encoder;
import org.lib.packet_processing.send.Encryptor;
import org.lib.packet_processing.send.PacketSenderThread;
import org.lib.packet_processing.strategies.StaticReceiversStrategy;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.lib.environment.EnvLoader.ENV_VARS;

public class UDPSocketThread extends Thread {
    @Getter private final PacketSenderThread senderThread;
    @Getter private final PacketReceiverThread receiverThread;
    @Getter private final String clientId = UUID.randomUUID().toString();

    public UDPSocketThread(PayloadRouter controller) throws IOException {
        var socket = new DatagramSocket();
        this.senderThread = new PacketSenderThread(socket, new Encoder(), new Encryptor(), new StaticReceiversStrategy(wrapServerAsReceiver()));
        this.receiverThread = new PacketReceiverThread(socket, controller, new Decoder(), new Decryptor(), null);
    }

    @Override
    public void run() {
        senderThread.start();
        receiverThread.start();
    }

    public void shutdown() {
        senderThread.interrupt();
        receiverThread.interrupt();
    }

    private InetSocketAddress getServerAddress() throws UnknownHostException {
        return new InetSocketAddress(
                InetAddress.getByName(ENV_VARS.get("UDP_SERVER_HOST")),
                Integer.parseInt(ENV_VARS.get("UDP_SERVER_PORT"))
        );
    }

    private Map<String, SocketAddress> wrapServerAsReceiver() throws UnknownHostException {
        var serverAddress = getServerAddress();
        Map<String, SocketAddress> serverMap = new ConcurrentHashMap<>();
        serverMap.put("server", serverAddress);
        return serverMap;
    }
}

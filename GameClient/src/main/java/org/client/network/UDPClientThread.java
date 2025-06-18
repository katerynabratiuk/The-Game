package org.client.network;

import lombok.Getter;
import org.client.game_logic.ClientController;
import org.lib.packet_processing.receive.Decoder;
import org.lib.packet_processing.receive.Decryptor;
import org.lib.packet_processing.receive.PacketReceiverThread;
import org.lib.packet_processing.send.Encoder;
import org.lib.packet_processing.send.Encryptor;
import org.lib.packet_processing.send.PacketSenderThread;
import org.lib.packet_processing.strategies.StaticReceiversStrategy;

import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.lib.environment.EnvLoader.ENV_VARS;

public class UDPClientThread extends Thread {
    private final DatagramSocket socket;
    @Getter private final PacketSenderThread senderThread;
    @Getter private final PacketReceiverThread receiverThread;
    @Getter private final String clientId = UUID.randomUUID().toString();
    private final Encoder encoder = new Encoder();
    private final Encryptor encryptor = new Encryptor();

    public UDPClientThread(ClientController controller) throws IOException {
        this.socket = new DatagramSocket();

        InetSocketAddress serverAddress = new InetSocketAddress(
                InetAddress.getByName(ENV_VARS.get("UDP_SERVER_HOST")),
                Integer.parseInt(ENV_VARS.get("UDP_SERVER_PORT"))
        );

        Set<SocketAddress> serverSet = Collections.singleton(serverAddress);

        this.senderThread = new PacketSenderThread(socket, encoder, encryptor, new StaticReceiversStrategy(serverSet));
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
}

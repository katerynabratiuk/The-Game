package org.client.network;

import lombok.Getter;
import org.client.gameLogic.ClientController;
import org.lib.PacketProcessing.receive.Decoder;
import org.lib.PacketProcessing.receive.Decryptor;
import org.lib.PacketProcessing.receive.PacketReceiverThread;
import org.lib.PacketProcessing.send.Encoder;
import org.lib.PacketProcessing.send.Encryptor;
import org.lib.PacketProcessing.send.PacketSenderThread;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.lib.Environment.EnvLoader.ENV_VARS;

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
        this.senderThread = new PacketSenderThread(socket, encoder, encryptor, serverSet);
        this.receiverThread = new PacketReceiverThread(socket, controller, new Decoder(), new Decryptor(), serverSet);
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

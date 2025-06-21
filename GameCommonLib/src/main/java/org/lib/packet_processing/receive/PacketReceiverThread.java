package org.lib.packet_processing.receive;

import org.lib.controllers.IController;
import org.lib.packet_processing.registry.SocketAddressRegistry;
import org.lib.packet_processing.serializers.Serializer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class PacketReceiverThread extends Thread {
    private final DatagramSocket socket;
    private final IController controller;
    private final IDecoder decoder;
    private final IDecryptor decryptor;
    private final SocketAddressRegistry registry;

    public PacketReceiverThread(DatagramSocket socket, IController controller, IDecoder decoder, IDecryptor decryptor, SocketAddressRegistry registry) {
        this.socket = socket;
        this.controller = controller;
        this.decoder = decoder;
        this.decryptor = decryptor;
        this.registry = registry;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[2048];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while (!socket.isClosed()) {
            try {
                socket.receive(packet);



                byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());
                byte[] decoded = decoder.decode(data);
                byte[] decrypted = decryptor.decrypt(decoded);
                var networkPayload = Serializer.deserialize(decrypted);

                // TODO: rewrite this null check - ambiguous logic
                // currently registry is passed only to server, and getClientUUID() != null is also only passed to server
                if (registry != null && networkPayload.getClientUUID() != null) {
                    registry.add(networkPayload.getClientUUID(), packet.getSocketAddress());
                }

                controller.register(networkPayload);
            } catch (IOException e) {
                if (socket.isClosed()) {
                    break;
                } else {
                    socket.close();
                }
            }
        }
    }
}

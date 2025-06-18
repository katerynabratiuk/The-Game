package org.lib.packet_processing.receive;

import org.lib.controllers.IController;
import org.lib.packet_processing.registry.SocketAddressRegistry;

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
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while (!socket.isClosed()) {
            try {
                socket.receive(packet);

                // TODO: rewrite this null check - ambiguous logic
                if (registry != null) {
                    registry.add(packet.getSocketAddress());
                }

                byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());
                byte[] decoded = decoder.decode(data);
                byte[] decrypted = decryptor.decrypt(decoded);

                controller.register(decrypted);
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

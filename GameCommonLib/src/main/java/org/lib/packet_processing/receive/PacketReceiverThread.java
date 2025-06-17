package org.lib.packet_processing.receive;

import org.lib.controllers.IController;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Set;

public class PacketReceiverThread extends Thread {
    private final DatagramSocket socket;
    private final IController controller;
    private final IDecoder decoder;
    private final IDecryptor decryptor;
    private final Set<SocketAddress> clients;

    public PacketReceiverThread(DatagramSocket socket, IController controller, IDecoder decoder, IDecryptor decryptor, Set<SocketAddress> clients) {
        this.socket = socket;
        this.controller = controller;
        this.decoder = decoder;
        this.decryptor = decryptor;
        this.clients = clients;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            while (!socket.isClosed()) {
                try {
                    socket.receive(packet);
                    clients.add(packet.getSocketAddress());
                    byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());
                    byte[] decoded = decoder.decode(data);
                    byte[] decrypted = decryptor.decrypt(decoded);

                    System.out.println("Starting registering at... " + Thread.currentThread().getName());
                    controller.register(decrypted);

                } catch (IOException e) {
                    if (socket.isClosed()) {
                        System.out.println("Receiver socket closed, exiting thread...");
                        break;
                    }
                }
            }
        } finally {
            if (!socket.isClosed()) {
                socket.close();
            }
        }
    }
}

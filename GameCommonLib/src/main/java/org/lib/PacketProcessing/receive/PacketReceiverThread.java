package org.lib.PacketProcessing.receive;

import lombok.SneakyThrows;
import org.lib.GameControllers.IController;

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

    @SneakyThrows
    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        var packet = new DatagramPacket(buffer, buffer.length);

        while (true) {
            try {
                socket.receive(packet);
                clients.add(packet.getSocketAddress());
                byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());

                byte[] decoded = decoder.decode(data);
                var decrypted = decryptor.decrypt(decoded);

                controller.register(decrypted);

            } catch (IOException e) {
                socket.close();
                e.printStackTrace();
            }
        }
    }
}

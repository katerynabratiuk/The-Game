package org.lib.PacketProcessing.receive;

import lombok.SneakyThrows;
import org.lib.GameControllers.IController;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class PacketReceiverThread extends Thread {
    private final DatagramSocket socket;
    private final IController controller;
    private final IDecoder decoder;
    private final IDecryptor decryptor;

    public PacketReceiverThread(DatagramSocket socket, IController controller, IDecoder decoder, IDecryptor decryptor) {
        this.socket = socket;
        this.controller = controller;
        this.decoder = decoder;
        this.decryptor = decryptor;
    }

    @SneakyThrows
    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        var packet = new DatagramPacket(buffer, buffer.length);

        while (true) {
            try {
                socket.receive(packet);
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

package org.lib.PacketProcessing.send;

import org.lib.DataStructures.ConcurrentQueue;
import org.lib.DataStructures.GameState;
import org.lib.PacketProcessing.receive.IDecoder;
import org.lib.PacketProcessing.receive.IDecryptor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PacketSenderThread extends Thread {
    private final DatagramSocket socket;
    private final ConcurrentQueue<OutgoingMessage> queue = new ConcurrentQueue<>();
    private final IEncoder encoder;
    private final IEncryptor encryptor;

    public PacketSenderThread(DatagramSocket socket, IEncoder encoder, IEncryptor encryptor) {
        this.socket = socket;
        this.encoder = encoder;
        this.encryptor = encryptor;
    }

    public void send(InetAddress address, int port, Object message) {
        queue.put(new OutgoingMessage(address, port, message));
    }

    @Override
    public void run() {
        while (true) {
            try {
                OutgoingMessage msg = queue.get();
                byte[] encrypted = encryptor.encrypt(msg);
                byte[] encoded = encoder.encode(encrypted);

                DatagramPacket dp = new DatagramPacket(encoded, encoded.length, msg.address, msg.port);
                socket.send(dp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class OutgoingMessage {
        InetAddress address;
        int port;
        Object payload;

        public OutgoingMessage(InetAddress address, int port, Object message) {
        }
    }
}


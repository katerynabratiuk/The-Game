package org.lib.PacketProcessing.send;

import org.lib.DataStructures.concurrency.ConcurrentQueue;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.Set;

public class PacketSenderThread extends Thread {
    private final DatagramSocket socket;
    private final ConcurrentQueue<byte[]> queue = new ConcurrentQueue<>();
    private final IEncoder encoder;
    private final IEncryptor encryptor;
    private final Set<SocketAddress> receivers;

    public PacketSenderThread(DatagramSocket socket, IEncoder encoder, IEncryptor encryptor, Set<SocketAddress> receivers) {
        this.socket = socket;
        this.encoder = encoder;
        this.encryptor = encryptor;
        this.receivers = receivers;
    }

    public void send(byte[] payload) {
        queue.put(payload);
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] payload = queue.get();
                byte[] encrypted = encryptor.encrypt(payload);
                byte[] encoded = encoder.encode(encrypted);

                for (SocketAddress client : receivers) {
                    DatagramPacket packet = new DatagramPacket(encoded, encoded.length, client);
                    socket.send(packet);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


package org.lib.packet_processing.send;

import org.lib.data_structures.concurrency.ConcurrentQueue;
import org.lib.data_structures.payloads.NetworkPayload;
import org.lib.packet_processing.registry.SocketAddressRegistry;
import org.lib.packet_processing.serializers.Serializer;
import org.lib.packet_processing.strategies.ReceiverStrategy;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class UnicastThread extends Thread {
    private final DatagramSocket socket;
    private final ConcurrentQueue<NetworkPayload> queue = new ConcurrentQueue<>();
    private final IEncoder encoder;
    private final IEncryptor encryptor;
    private final SocketAddressRegistry registry;

    public UnicastThread(DatagramSocket socket, IEncoder encoder, IEncryptor encryptor, SocketAddressRegistry registry) {
        this.socket = socket;
        this.encoder = encoder;
        this.encryptor = encryptor;
        this.registry = registry;
    }

    public void send(NetworkPayload payload) {
        queue.put(payload);
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                try {
                    NetworkPayload payload = queue.get();
                    byte[] serialized = Serializer.serialize(payload);
                    byte[] encrypted = encryptor.encrypt(serialized);
                    byte[] encoded = encoder.encode(encrypted);

                    var receiverAddress = registry.get(payload.getClientUUID());
                    var packet = new DatagramPacket(encoded, encoded.length, receiverAddress);
                    socket.send(packet);
                    System.out.println("Packet sent to " + receiverAddress);
                } catch (Exception e) {
                    if (socket.isClosed()) {
                        break;
                    } else {
                        socket.close();
                    }
                }
            }
        } finally {
            System.out.println("Closed Sender Thread loop...");
        }
    }
}


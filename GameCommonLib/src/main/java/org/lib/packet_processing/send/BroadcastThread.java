package org.lib.packet_processing.send;

import org.lib.data_structures.concurrency.ConcurrentQueue;
import org.lib.data_structures.payloads.NetworkPayload;
import org.lib.packet_processing.registry.IReceiverRegistryObserver;
import org.lib.packet_processing.serializers.Serializer;
import org.lib.packet_processing.strategies.ReceiverStrategy;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class BroadcastThread extends Thread implements IReceiverRegistryObserver {
    private final DatagramSocket socket;
    private final ConcurrentQueue<NetworkPayload> queue = new ConcurrentQueue<>();
    private final IEncoder encoder;
    private final IEncryptor encryptor;
    private final ReceiverStrategy receiverStrategy;
    private final Object receiversLock = new Object();

    public BroadcastThread(DatagramSocket socket, IEncoder encoder, IEncryptor encryptor, ReceiverStrategy receiverStrategy) {
        this.socket = socket;
        this.encoder = encoder;
        this.encryptor = encryptor;
        this.receiverStrategy = receiverStrategy;
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

                    for (SocketAddress receiver : receiverStrategy.getReceivers()) {
                        DatagramPacket packet = new DatagramPacket(encoded, encoded.length, receiver);
                        socket.send(packet);
                        System.out.println("Packet sent to " + receiver);
                    }
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

    public synchronized boolean hasReceivers() {
        return !receiverStrategy.getReceivers().isEmpty();
    }

    public synchronized void removeReceiver(String clientUUID) {
        receiverStrategy.removeReceiver(clientUUID);
    }

    public void waitForReceivers() throws InterruptedException {
        synchronized (receiversLock) {
            while (!hasReceivers()) {
                receiversLock.wait();
            }
        }
    }

    @Override
    public void onReceiverAdded() {
        synchronized (receiversLock) {
            receiversLock.notifyAll(); // wake up GameThread
        }
    }
}


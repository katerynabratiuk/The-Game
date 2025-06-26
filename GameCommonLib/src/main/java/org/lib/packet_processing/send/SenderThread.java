package org.lib.packet_processing.send;

import org.lib.data.concurrency.ConcurrentQueue;
import org.lib.data.payloads.NetworkPayload;
import org.lib.packet_processing.registry.IReceiverRegistryObserver;
import org.lib.packet_processing.send.strategies.BroadcastStrategy;
import org.lib.packet_processing.send.strategies.IReceiversResolveStrategy;
import org.lib.packet_processing.serializers.BsonSerializer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class SenderThread extends Thread implements IReceiverRegistryObserver {
    private final DatagramSocket socket;
    private final IEncoder encoder;
    private final IEncryptor encryptor;
    private final IReceiversResolveStrategy strategy;
    private final ConcurrentQueue<NetworkPayload> queue;
    private final Object receiversLock = new Object();

    public SenderThread(DatagramSocket socket, IEncoder encoder, IEncryptor encryptor, IReceiversResolveStrategy strategy, ConcurrentQueue<NetworkPayload> incomingQueue) {
        this.socket = socket;
        this.encoder = encoder;
        this.encryptor = encryptor;
        this.strategy = strategy;
        this.queue = incomingQueue;
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
                    byte[] serialized = BsonSerializer.serialize(payload);
                    byte[] encrypted = encryptor.encrypt(serialized);
                    byte[] encoded = encoder.encode(encrypted);

                    for (SocketAddress receiver : strategy.resolveReceivers(payload)) {
                        DatagramPacket packet = new DatagramPacket(encoded, encoded.length, receiver);
                        socket.send(packet);
                    }
                } catch (Exception e) {
                    if (socket.isClosed()) break;
                    socket.close();
                }
            }
        } finally {
            System.out.println("Closed Sender Thread loop...");
        }
    }

    public void waitForReceivers() throws InterruptedException {
        synchronized (receiversLock) {
            while (strategy instanceof BroadcastStrategy bs && bs.noReceivers()) {
                receiversLock.wait();
            }
        }
    }

    public void removeReceiver(String clientUUID) {
        if (strategy instanceof BroadcastStrategy bs) {
            bs.removeReceiver(clientUUID);
        }
    }

    @Override
    public void onReceiverAdded() {
        synchronized (receiversLock) {
            receiversLock.notifyAll();
        }
    }

    public synchronized boolean noReceivers() {
        if (strategy instanceof BroadcastStrategy bs) {
            return bs.noReceivers();
        }
        return true;
    }
}

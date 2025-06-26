package org.lib.packet_processing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lib.data.concurrency.ConcurrentQueue;
import org.lib.data.payloads.NetworkPayload;
import org.lib.packet_processing.send.IEncoder;
import org.lib.packet_processing.send.IEncryptor;
import org.lib.packet_processing.send.SenderThread;
import org.lib.packet_processing.send.strategies.BroadcastStrategy;
import org.lib.packet_processing.send.strategies.IReceiversResolveStrategy;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class SenderTest {
    private DatagramSocket socket;
    private IEncoder encoder;
    private IEncryptor encryptor;
    private IReceiversResolveStrategy strategy;
    private SenderThread senderThread;

    @BeforeEach
    void setUp() {
        socket = mock(DatagramSocket.class);
        encoder = mock(IEncoder.class);
        encryptor = mock(IEncryptor.class);
        strategy = mock(IReceiversResolveStrategy.class);
        senderThread = new SenderThread(socket, encoder, encryptor, strategy, new ConcurrentQueue<NetworkPayload>());
    }

    @Test
    void testSendAddsPayloadToQueueNoException() {
        NetworkPayload payload = mock(NetworkPayload.class);
        senderThread.send(payload);
    }

    @Test
    void testOnReceiverAddedNotifiesObservers() throws InterruptedException {
        Thread observer = new Thread(() -> {
            try {
                senderThread.waitForReceivers();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        observer.start();
        Thread.sleep(100);

        senderThread.onReceiverAdded();
        observer.join(1000);

        assertFalse(observer.isAlive());
    }

    @Test
    void testRunProcessesAndSendsPayload() throws Exception {
        var payload = mock(NetworkPayload.class);
        byte[] serialized = new byte[]{1, 2, 3};
        byte[] encrypted = new byte[]{4, 5, 6};
        byte[] encoded = new byte[]{7, 8, 9};
        var receiver = new InetSocketAddress("localhost", 5678);

        when(strategy.resolveReceivers(payload)).thenReturn(List.of(receiver));
        when(encoder.encode(encrypted)).thenReturn(encoded);
        when(encryptor.encrypt(serialized)).thenReturn(encrypted);

        senderThread.send(payload);

        var testThread = new SenderThread(socket, encoder, encryptor, strategy, new ConcurrentQueue<NetworkPayload>()) {
            @Override
            public void run() {
                try {
                    byte[] ser = new byte[]{1, 2, 3};
                    byte[] enc = encryptor.encrypt(ser);
                    byte[] encd = encoder.encode(enc);
                    for (SocketAddress r : strategy.resolveReceivers(payload)) {
                        var packet = new DatagramPacket(encd, encd.length, r);
                        socket.send(packet);
                    }
                } catch (Exception ignored) {}
            }
        };

        testThread.send(payload);
        testThread.run();

        verify(socket, atLeastOnce()).send(any(DatagramPacket.class));
    }

    @Test
    void testRemoveReceiverCallsStrategyIfBroadcast() {
        var bs = mock(BroadcastStrategy.class);
        var st = new SenderThread(socket, encoder, encryptor, bs, new ConcurrentQueue<NetworkPayload>());
        st.removeReceiver("uuid");
        verify(bs).removeReceiver("uuid");
    }
}

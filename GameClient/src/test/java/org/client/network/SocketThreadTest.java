package org.client.network;

import org.client.game_logic.PayloadRouter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lib.packet_processing.receive.PacketReceiverThread;
import org.lib.packet_processing.send.SenderThread;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class SocketThreadTest {
    private UDPSocketThread udpSocketThread;

    @BeforeEach
    void setUp() {
        var controller = mock(PayloadRouter.class);
        udpSocketThread = spy(new UDPSocketThread(controller));
    }

    @Test
    void testShutdown_interruptsSenderAndReceiverThreads() throws Exception {
        var senderThread = mock(SenderThread.class);
        var receiverThread = mock(PacketReceiverThread.class);

        udpSocketThread.setSenderThread(senderThread);
        udpSocketThread.setReceiverThread(receiverThread);

        udpSocketThread.shutdown();

        verify(senderThread).interrupt();
        verify(receiverThread).interrupt();
        assertFalse(udpSocketThread.isAlive());
    }
}

package org.client.network;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lib.data.payloads.NetworkPayload;
import org.lib.data.payloads.Payload;
import org.lib.data.payloads.enums.ConnectionCode;
import org.lib.data.payloads.game.PlayerInput;
import org.lib.data.payloads.network.ConnectionRequest;
import org.lib.data.payloads.queries.RegisterPayload;
import org.lib.packet_processing.send.SenderThread;
import org.mockito.ArgumentCaptor;

import java.io.IOException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PacketsSenderServiceTest {

    private UDPSocketThread clientThread;
    private SenderThread senderThread;
    private PacketsSenderService service;

    @BeforeEach
    void setUp() throws IOException {
        clientThread = mock(UDPSocketThread.class);
        senderThread = mock(SenderThread.class);
        when(clientThread.getSenderThread()).thenReturn(senderThread);
        when(clientThread.getClientId()).thenReturn("client");

        service = new PacketsSenderService(clientThread);
    }

    @Test
    void testSendInput_callsSenderThreadSendWithCorrectPayload() {
        var input = mock(PlayerInput.class);

        service.sendInput(input);

        var captor = ArgumentCaptor.forClass(NetworkPayload.class);
        verify(senderThread).send(captor.capture());
        var payload = captor.getValue();
        assertEquals("client", payload.getClientUUID());
        assertEquals(1, payload.getPayloads().size());
        assertSame(input, payload.getPayloads().get(0));
    }

    @Test
    void testSendJoinRequest_sendsConnectionRequestWithJoinCode() {
        service.sendJoinRequest();

        ArgumentCaptor<NetworkPayload> captor = ArgumentCaptor.forClass(NetworkPayload.class);
        verify(senderThread).send(captor.capture());

        NetworkPayload payload = captor.getValue();
        Payload p = payload.getPayloads().get(0);
        assertInstanceOf(ConnectionRequest.class, p);
        assertEquals(ConnectionCode.JOIN, ((ConnectionRequest) p).getConnectionCode());
    }

    @Test
    void testSendRegister_setsClientUUIDFromClientThread() {
        String username = "user";
        String password = "pass";

        service.sendRegister(username, password);

        var captor = ArgumentCaptor.forClass(NetworkPayload.class);
        verify(senderThread).send(captor.capture());

        var payload = captor.getValue();
        var p = payload.getPayloads().get(0);
        assertInstanceOf(RegisterPayload.class, p);
        assertEquals("client", p.getClientUUID());
        assertEquals(username, ((RegisterPayload) p).getUsername());
        assertEquals(password, ((RegisterPayload) p).getPassword());
    }

    @Test
    void testShutdown_callsClientThreadShutdown() {
        service.shutdown();

        verify(clientThread).shutdown();
    }

    @Test
    void testSend_doesNotSendIfSenderThreadNull() throws IOException {
        when(clientThread.getSenderThread()).thenReturn(null);
        var serviceWithNullSender = new PacketsSenderService(clientThread);
        var input = mock(PlayerInput.class);

        serviceWithNullSender.sendInput(input);

        verify(senderThread, never()).send(any());
    }
}

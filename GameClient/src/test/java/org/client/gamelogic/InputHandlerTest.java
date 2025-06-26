package org.client.gamelogic;

import org.client.UI.MapPanel;
import org.client.game_logic.InputHandler;
import org.client.network.PacketsSenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lib.data.payloads.game.PlayerInput;
import org.mockito.ArgumentCaptor;

import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InputHandlerTest {

    private PacketsSenderService packetsSenderService;
    private MapPanel mapPanel;
    private InputHandler inputHandler;

    @BeforeEach
    void setUp() {
        packetsSenderService = mock(PacketsSenderService.class);
        mapPanel = mock(MapPanel.class);

        when(packetsSenderService.getClientId()).thenReturn("client");

        inputHandler = new InputHandler(mapPanel, packetsSenderService);
    }

    @Test
    void testConstructorSetsInputCallback() {
        verify(mapPanel, times(1)).setInputCallback(inputHandler);
    }

    @Test
    void testOnKeyPressedSendsInput() {
        int keyCode = 42;

        inputHandler.onKeyPressed(keyCode);

        var captor = ArgumentCaptor.forClass(PlayerInput.class);
        verify(packetsSenderService).sendInput(captor.capture());
        var sentInput = captor.getValue();
        assertEquals("client", sentInput.getClientUUID());
        assertEquals(keyCode, sentInput.getKeyInputCode());
        assertFalse(sentInput.isKeyReleased());
    }

    @Test
    void testOnKeyReleasedSendsInputWithReleasedFlag() {
        int keyCode = 99;

        inputHandler.onKeyReleased(keyCode);

        var captor = ArgumentCaptor.forClass(PlayerInput.class);
        verify(packetsSenderService).sendInput(captor.capture());
        var sentInput = captor.getValue();
        assertEquals("client", sentInput.getClientUUID());
        assertEquals(keyCode, sentInput.getKeyInputCode());
        assertTrue(sentInput.isKeyReleased());
    }

    @Test
    void testOnMouseClickedSendsInputWithDirection() {
        int x = 10;
        int y = 20;

        inputHandler.onMouseClicked(x, y);

        var captor = ArgumentCaptor.forClass(PlayerInput.class);
        verify(packetsSenderService).sendInput(captor.capture());
        var sentInput = captor.getValue();
        assertEquals("client", sentInput.getClientUUID());
        assertEquals(MouseEvent.BUTTON1, sentInput.getKeyInputCode());
        assertNotNull(sentInput.getDirection());
        assertEquals(x, sentInput.getDirection().getX());
        assertEquals(y, sentInput.getDirection().getY());
    }
}
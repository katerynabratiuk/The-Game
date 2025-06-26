package org.server.gamelogic;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lib.data.payloads.actors.Actor;
import org.lib.data.payloads.actors.Bullet;
import org.lib.data.payloads.actors.Coordinates;
import org.lib.data.payloads.actors.PlayerCharacter;
import org.lib.data.payloads.game.PlayerInput;
import org.lib.data.payloads.game.Vector;
import org.server.game_logic.KeyBindingsHandler;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KeyBindingsHandlerTest {
    private PlayerCharacter player;
    private List<Actor> actors;
    private final String clientUUID = "test";

    @BeforeEach
    void setUp() {
        player = mock(PlayerCharacter.class);
        when(player.getCoordinates()).thenReturn(new Coordinates(100, 100));
        when(player.getMovementSpeed()).thenReturn(5.0);
        when(player.determineAttackReady()).thenReturn(true);
        when(player.getDamage()).thenReturn(10.0);

        actors = new ArrayList<>();
    }

    @Test
    void testUpdateKeyStateAndMovement() {
        KeyBindingsHandler.updateKeyState(clientUUID, KeyEvent.VK_W, true);

        Actor movableActor = mock(Actor.class);
        when(movableActor.getCoordinates()).thenReturn(new Coordinates(0, 0));
        when(movableActor.getMovementSpeed()).thenReturn(4.0);

        KeyBindingsHandler.processInput(movableActor, actors, createInput(null, 0));

        verify(movableActor).setCoordinates(argThat(coord ->
                coord.getX() == 0 && coord.getY() == -4
        ));
    }

    @Test
    void testShootAddsBulletToActors() {
        PlayerInput input = createInput(new Vector(1, 0), MouseEvent.BUTTON1);

        KeyBindingsHandler.processInput(player, actors, input);

        assertEquals(1, actors.size());
        assertTrue(actors.get(0) instanceof Bullet);
        Bullet bullet = (Bullet) actors.get(0);

        assertEquals(clientUUID, bullet.getClientUUID());
        assertEquals(10, bullet.getDamage());
        assertEquals(100, bullet.getCoordinates().getX());
        assertEquals(100, bullet.getCoordinates().getY());

        verify(player).setLastAttackTime(anyLong());
    }

    @Test
    void testProcessInputWithNullInputDoesNothing() {
        KeyBindingsHandler.processInput(player, actors, null);
        assertTrue(actors.isEmpty());
        verify(player, never()).setCoordinates(any());
    }

    private PlayerInput createInput(Vector direction, int keyCode) {
        PlayerInput input = mock(PlayerInput.class);
        when(input.getClientUUID()).thenReturn(clientUUID);
        when(input.getDirection()).thenReturn(direction);
        when(input.getKeyInputCode()).thenReturn(keyCode);
        return input;
    }
}
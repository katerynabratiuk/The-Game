package org.server.gamelogic;


import org.junit.jupiter.api.Test;
import org.lib.data.payloads.actors.Actor;
import org.lib.data.payloads.actors.Coordinates;
import org.server.game_logic.CollisionHandler;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

class CollisionHandlerTest {

    @Test
    void testCheckCollisions_triggersOnKillAndOnDestroy() {
        var actor1 = mock(Actor.class);
        var actor2 = mock(Actor.class);

        when(actor1.getCoordinates()).thenReturn(new Coordinates(0, 0));
        when(actor2.getCoordinates()).thenReturn(new Coordinates(1, 0));
        when(actor1.getRadius()).thenReturn(1.0);
        when(actor2.getRadius()).thenReturn(1.0);

        when(actor1.isKilled()).thenReturn(true);
        when(actor1.isPendingDestroy()).thenReturn(true);

        var handler = new CollisionHandler();
        var onKill = mock(BiConsumer.class);
        var onDestroy = mock(Consumer.class);

        handler.checkCollisions(List.of(actor1, actor2), onKill, onDestroy);

        verify(actor1).OnCollision(actor2);
        verify(onKill).accept(actor1, actor2);
        verify(onDestroy).accept(actor1);
    }
}
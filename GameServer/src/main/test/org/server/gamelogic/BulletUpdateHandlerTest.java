package org.server.gamelogic;

import org.junit.jupiter.api.Test;
import org.lib.data.payloads.actors.Bullet;
import org.server.game_logic.BulletUpdateHandler;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BulletUpdateHandlerTest {

    @Test
    void testUpdateAndCleanupBullets_removesPendingDestroyBullets() {
        var bullet1 = mock(Bullet.class);
        var bullet2 = mock(Bullet.class);

        when(bullet1.isPendingDestroy()).thenReturn(true);
        when(bullet2.isPendingDestroy()).thenReturn(false);

        var handler = new BulletUpdateHandler();
        var removed = handler.updateAndCleanupBullets(List.of(bullet1, bullet2));

        verify(bullet1).onNextFrame();
        verify(bullet2).onNextFrame();

        assertEquals(1, removed.size());
        assertTrue(removed.contains(bullet1));
    }
}
package org.server.gamelogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lib.data.payloads.actors.Actor;
import org.lib.data.payloads.actors.PlayerCharacter;
import org.server.game_logic.BulletUpdateHandler;
import org.server.game_logic.CollisionHandler;
import org.server.game_logic.GameStateManager;
import org.server.game_logic.PlayersStatsTracker;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameStateManagerTest {

    private GameStateManager manager;
    private PlayersStatsTracker statsTracker;
    private CollisionHandler collisionHandler;
    private BulletUpdateHandler bulletUpdater;

    @BeforeEach
    void setUp() {
        statsTracker = mock(PlayersStatsTracker.class);
        collisionHandler = mock(CollisionHandler.class);
        bulletUpdater = mock(BulletUpdateHandler.class);
        manager = new GameStateManager(statsTracker, collisionHandler, bulletUpdater);
    }

    @Test
    void testAddActorAndGetAllActors() {
        var actor = mock(Actor.class);
        manager.addActor(actor);

        List<Actor> result = manager.getAllActors();
        assertEquals(1, result.size());
        assertTrue(result.contains(actor));
    }

    @Test
    void testRemoveActor_RemovesCorrectPlayerCharacter() {
        var pc = mock(PlayerCharacter.class);
        when(pc.getClientUUID()).thenReturn("123-123");
        manager.addActor(pc);

        manager.removeActor("123-123");
        assertFalse(manager.getAllActors().contains(pc));
    }

    @Test
    void testRegisterAndGetUsername() {
        manager.registerUsername("123-123", "test");

        String result = manager.getUsernameByClientUUID("123-123");
        assertEquals("test", result);
    }

    @Test
    void testSnapshot_ReturnsExpectedState() {
        PlayerCharacter pc = mock(PlayerCharacter.class);
        when(pc.getClientUUID()).thenReturn("123-123");
        manager.addActor(pc);
        manager.registerUsername("123-123", "test");

        when(statsTracker.getKillsSnapshot(anyCollection())).thenReturn(Map.of("test", 5));

        var snapshot = manager.snapshot();

        assertEquals(1, snapshot.getActors().size());
        assertEquals(5, snapshot.getPlayerKills().get("test"));
    }
}
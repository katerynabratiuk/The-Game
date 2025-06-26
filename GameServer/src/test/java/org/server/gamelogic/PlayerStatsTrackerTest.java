package org.server.gamelogic;


import org.junit.jupiter.api.Test;
import org.server.game_logic.PlayersStatsTracker;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStatsTrackerTest {
    @Test
    void testIncrementKill_andSnapshot() {
        var tracker = new PlayersStatsTracker();

        tracker.incrementKill("a");
        tracker.incrementKill("a");

        Map<String, Integer> snapshot = tracker.getKillsSnapshot(Set.of("a", "b"));

        assertEquals(2, snapshot.get("a"));
        assertEquals(0, snapshot.get("b"));
    }
}

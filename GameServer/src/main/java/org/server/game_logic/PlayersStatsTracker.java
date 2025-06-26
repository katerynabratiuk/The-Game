package org.server.game_logic;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayersStatsTracker {
    private final Map<String, Integer> playerKills = new ConcurrentHashMap<>();

    public void incrementKill(String username) {
        playerKills.put(username, playerKills.getOrDefault(username, 0) + 1);
    }

    public Map<String, Integer> getKillsSnapshot(Collection<String> usernames) {
        Map<String, Integer> snapshot = new ConcurrentHashMap<>();
        for (String user : usernames) {
            snapshot.put(user, playerKills.getOrDefault(user, 0));
        }
        return snapshot;
    }
}
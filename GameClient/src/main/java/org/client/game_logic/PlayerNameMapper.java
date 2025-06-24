package org.client.game_logic;

import org.lib.data_structures.payloads.actors.PlayerCharacter;
import org.lib.data_structures.payloads.game.GameState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class PlayerNameMapper {
    private static final Map<String, String> clientUUIDToUsernameLookup = new ConcurrentHashMap<>();

    public static void registerPlayer(String clientUUID, String username) {
        clientUUIDToUsernameLookup.put(clientUUID, username);
    }

    public static String getUsername(String clientUUID) {
        return clientUUIDToUsernameLookup.get(clientUUID);
    }

    public static void clear() {
        clientUUIDToUsernameLookup.clear();
    }

    public static void removePlayer(String clientUUID) {
        clientUUIDToUsernameLookup.remove(clientUUID);
    }

    public static int getMappedCount() {
        return clientUUIDToUsernameLookup.size();
    }

    public static java.util.Set<String> getAllMappedUUIDs() {
        return clientUUIDToUsernameLookup.keySet();
    }

    public static boolean isUsernameMapped(String username) {
        return clientUUIDToUsernameLookup.containsValue(username);
    }

    public static void updatePlayerNameMappings(GameState gameState) {
        // kills map with usernames
        Map<String, Integer> kills = gameState.getPlayerKills();
        if (kills == null) return;

        // get all actors
        var players = gameState.getActorsSnapshot().stream()
                .filter(actor -> actor instanceof PlayerCharacter)
                .toList();

        // register new usernames
        if (kills.size() > getMappedCount()) {
            var usernames = kills.keySet().toArray(new String[0]);
            var mappedUUIDs = getAllMappedUUIDs();

            for (var player : players) {
                String clientUUID = player.getClientUUID();
                if (!mappedUUIDs.contains(clientUUID)) {
                    for (String username : usernames) {
                        if (!isUsernameMapped(username)) {
                            registerPlayer(clientUUID, username);
                            break;
                        }
                    }
                }
            }
        }
    }
} 
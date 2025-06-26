package org.server.game_logic;

import lombok.Getter;
import org.lib.data_structures.payloads.NetworkPayload;
import org.lib.data_structures.payloads.actors.PlayerCharacter;
import org.lib.data_structures.payloads.game.*;
import org.lib.data_structures.payloads.actors.Actor;
import org.lib.data_structures.payloads.actors.Bullet;
import org.lib.packet_processing.send.SenderThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.lib.data_structures.payloads.enums.NotificationCode.KILL;

@Getter
public class GameStateManager {
    private final List<Actor> actors = new CopyOnWriteArrayList<>();
    private final Map<String, Integer> playerKills = new ConcurrentHashMap<>();
    private final Map<String, String> clientUUIDToUsernameLookup = new ConcurrentHashMap<>();

    public synchronized void updateGameThread(SenderThread unicast, SenderThread broadcast) {
        updateBullets();
        checkCollision(unicast);
    }

    private synchronized void updateBullets() {
        List<Actor> toRemove = new ArrayList<>();

        for (Actor actor : actors) {
            if (actor instanceof Bullet bullet) {
                bullet.onNextFrame();
                if (bullet.isPendingDestroy()) {
                    toRemove.add(bullet);
                }
            }
        }

        actors.removeAll(toRemove);
    }

    private synchronized void checkCollision(SenderThread unicastThread) {
        List<Actor> actorsToRemove = new ArrayList<>();

        for (Actor trigger : actors) {
            for (Actor target : actors) {
                if (target == trigger) continue;
                if (isCollision(trigger, target)) {
                    trigger.OnCollision(target);
                }
                if (trigger.isKilled()) {
                    updatePlayerKills(target);
                    sendKillNotification(unicastThread, trigger);
                }
            }

            if (trigger.isPendingDestroy()) {
                actorsToRemove.add(trigger);
            }
        }

        actors.removeAll(actorsToRemove);
    }

    public synchronized GameState snapshot() {
        Map<String, Integer> completePlayerKills = new ConcurrentHashMap<>();
        
        for (String username : clientUUIDToUsernameLookup.values()) {
            completePlayerKills.put(username, playerKills.getOrDefault(username, 0));
        }
        
        return new GameState(new ArrayList<>(actors), completePlayerKills);
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public List<Actor> getAllActors() {
        return actors;
    }

    public Actor getPlayerCharacterByUUID(String clientUUID) {
        return actors.stream()
                .filter(a -> a.getClientUUID().equals(clientUUID) && (a instanceof PlayerCharacter))
                .findFirst()
                .orElse(null);
    }

    public void removeActor(String clientUUID) {
        var actor = getPlayerCharacterByUUID(clientUUID);
        if (actor != null) actors.remove(actor);
    }

    public void registerUsername(String clientUUID, String username) {
        clientUUIDToUsernameLookup.put(clientUUID, username);
    }

    public String getUsernameByClientUUID(String clientUUID) {
        return clientUUIDToUsernameLookup.get(clientUUID);
    }

    private void sendKillNotification(SenderThread thread, Actor actor) {
        var notif = new Notification("You were killed", KILL);
        thread.send(new NetworkPayload(List.of(notif), actor.getClientUUID()));
    }

    private boolean isCollision(Actor trigger, Actor target) {
        double x = trigger.getCoordinates().getX() - target.getCoordinates().getX();
        double y = trigger.getCoordinates().getY() - target.getCoordinates().getY();
        double distance = Math.sqrt(x*x + y*y);

        double radSum = trigger.getRadius() + target.getRadius();
        return distance <= radSum;
    }

    private void updatePlayerKills(Actor killer) {
        var killerCharacter = (PlayerCharacter) getPlayerCharacterByUUID(killer.getClientUUID());
        if (killerCharacter != null) {
            incrementPlayerKills(killerCharacter.getUsername());
            System.out.println("Incremented player kills: " + killerCharacter.getUsername());
        }
    }

    private void incrementPlayerKills(String username) {
        playerKills.put(username, playerKills.getOrDefault(username, 0) + 1);
    }
}

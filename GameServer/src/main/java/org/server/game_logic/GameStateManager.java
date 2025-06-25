package org.server.game_logic;

import lombok.Getter;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.lib.data_structures.payloads.NetworkPayload;
import org.lib.data_structures.payloads.actors.PlayerCharacter;
import org.lib.data_structures.payloads.game.*;
import org.lib.data_structures.payloads.actors.Actor;
import org.lib.data_structures.payloads.actors.Bullet;
import org.lib.packet_processing.send.BroadcastThread;
import org.lib.packet_processing.send.UnicastThread;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.lib.data_structures.payloads.enums.NotificationCode.KILL;

public class GameStateManager {
    private final List<Actor> actors = new CopyOnWriteArrayList<>();
    @Getter
    private final Map<String, Integer> playerKills = new ConcurrentHashMap<>();
    private final Map<String, String> clientUUIDToUsernameLookup = new ConcurrentHashMap<>();

    public synchronized void updateGameThread(UnicastThread unicast, BroadcastThread broadcast) {
        updateBullets();
        checkCollision(unicast);
    }

    private synchronized void updateBullets() {
        // rewrite with pendingDestroy
        List<Actor> toRemove = new ArrayList<>();

        for (Actor actor : actors) {
            if (actor instanceof Bullet bullet) {
                long currentTime = System.currentTimeMillis();

                if (currentTime - bullet.getCreationTime() >= bullet.getLifespan()) {
                    toRemove.add(bullet);
                    continue;
                }

                var current = new Vector2D(bullet.getCoordinates().getX(), bullet.getCoordinates().getY());
                var direction = Vector.toVector2D(bullet.getDirection());
                if (direction.getNorm() == 0) continue;

                var movement = direction.normalize().scalarMultiply(bullet.getMovementSpeed());
                var newPos = current.add(movement);

                bullet.setCoordinates(new Coordinates((int) newPos.getX(), (int) newPos.getY()));
            }
        }

        actors.removeAll(toRemove);
    }

    private synchronized void checkCollision(UnicastThread thread) {
        List<Actor> actorsToRemove = new ArrayList<>();

        for (Actor trigger : actors) {
            for (Actor target : actors) {
                if (target == trigger) continue;
                if (isCollision(trigger, target)) {
                    trigger.OnCollision(target);
                }
                if (trigger.isKilled()) {
                    updatePlayerKills(target);
                    sendKillNotification(thread, trigger);
                }
            }

            if (trigger.isPendingDestroy()) {
                actorsToRemove.add(trigger);
            }
        }

        actors.removeAll(actorsToRemove);
    }

    private void sendKillNotification(UnicastThread thread, Actor actor) {
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

    public void loginUsername(String clientUUID, String username) {
        clientUUIDToUsernameLookup.put(clientUUID, username);
    }

    public String getUsernameByClientUUID(String clientUUID) {
        return clientUUIDToUsernameLookup.get(clientUUID);
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

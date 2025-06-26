package org.server.game_logic;

import lombok.Getter;
import org.lib.data_structures.payloads.NetworkPayload;
import org.lib.data_structures.payloads.actors.PlayerCharacter;
import org.lib.data_structures.payloads.game.*;
import org.lib.data_structures.payloads.actors.Actor;
import org.lib.packet_processing.send.SenderThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static org.lib.data_structures.payloads.enums.NotificationCode.KILL;

@Getter
public class GameStateManager {
    private final List<Actor> actors = new CopyOnWriteArrayList<>();
    private final Map<String, String> clientUUIDToUsernameLookup = new ConcurrentHashMap<>();
    private final PlayersStatsTracker statsTracker;
    private final CollisionHandler collisionHandler;
    private final BulletUpdateHandler bulletUpdater;

    public GameStateManager(PlayersStatsTracker statsTracker, CollisionHandler collisionHandler, BulletUpdateHandler bulletUpdater) {
        this.statsTracker = statsTracker;
        this.collisionHandler = collisionHandler;
        this.bulletUpdater = bulletUpdater;
    }

    public synchronized void updateGameThread(SenderThread unicast, SenderThread broadcast) {
        var bulletsToRemove = bulletUpdater.updateAndCleanupBullets(actors);
        var collidedToRemove = new ArrayList<Actor>();

        Consumer<Actor> onKillCallback = killed -> {
            updatePlayerKills(killed);
            sendKillNotification(unicast, killed);
        };
        Consumer<Actor> onDestroyCallback = collidedToRemove::add;
        collisionHandler.checkCollisions(actors, onKillCallback, onDestroyCallback);

        List<Actor> allToRemove = new ArrayList<>(bulletsToRemove);
        allToRemove.addAll(collidedToRemove);
        actors.removeAll(allToRemove);
    }

    public synchronized GameState snapshot() {
        return new GameState(
                new ArrayList<>(actors),
                statsTracker.getKillsSnapshot(clientUUIDToUsernameLookup.values())
        );
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public List<Actor> getAllActors() {
        return actors;
    }

    public Actor getPlayerCharacterByUUID(String clientUUID) {
        return actors.stream()
                .filter(a -> a.getClientUUID().equals(clientUUID) && a instanceof PlayerCharacter)
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

    private void updatePlayerKills(Actor killedActor) {
        var killer = getPlayerCharacterByUUID(killedActor.getClientUUID());
        if (killer instanceof PlayerCharacter pc) {
            statsTracker.incrementKill(pc.getUsername());
            System.out.println("Incremented player kills: " + pc.getUsername());
        }
    }
}

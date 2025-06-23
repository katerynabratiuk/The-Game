package org.server.game_logic;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.lib.data_structures.payloads.Coordinates;
import org.lib.data_structures.payloads.Vector;
import org.lib.data_structures.payloads.actors.Actor;
import org.lib.data_structures.payloads.GameState;
import org.lib.data_structures.payloads.PlayerInput;
import org.lib.data_structures.payloads.actors.Bullet;

import java.util.ArrayList;
import java.util.List;

public class GameStateManager {
    private final List<Actor> actors = new ArrayList<>();
    private boolean friendlyFireEnabled = false;
    // add queue for updates?

    public synchronized void updateGameStateByInput(PlayerInput input) {
        var actor = getActorByClientUUID(input.getClientUUID());
        KeyBindingsHandler.handleInput(input.getKeyInputCode(), actor, actors, input);
    }

    public synchronized void updateGameThread() {
        updateBullets();
        checkCollision();
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

    private synchronized void checkCollision() {
        List<Actor> actorsToRemove = new ArrayList<>();

        for (Actor trigger : actors) {
            for (Actor target : actors) {
                if (target == trigger) continue;

                if (isCollision(trigger, target)) {
                    trigger.OnCollision(target);
                }
            }

            if (trigger.isPendingDestroy()) {
                actorsToRemove.add(trigger);
            }
        }

        actors.removeAll(actorsToRemove);
    }

    private boolean isCollision(Actor trigger, Actor target) {
        double x = trigger.getCoordinates().getX() - target.getCoordinates().getX();
        double y = trigger.getCoordinates().getY() - target.getCoordinates().getY();
        double distance = Math.sqrt(x*x + y*y);

        double radSum = trigger.getRadius() + target.getRadius();
        return distance <= radSum;
    }

    public synchronized GameState snapshot() {
        return new GameState(new ArrayList<>(actors));
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public List<Actor> getAllActors() {
        return actors;
    }

    public Actor getActorByClientUUID(String clientUUID) {
        return actors.stream()
                .filter(a -> a.getClientUUID().equals(clientUUID))
                .findFirst()
                .orElseThrow();
    }

    public void removeActor(String clientUUID) {
        var actor = getActorByClientUUID(clientUUID);
        if (actor == null) return;
        actors.remove(actor);
    }
}

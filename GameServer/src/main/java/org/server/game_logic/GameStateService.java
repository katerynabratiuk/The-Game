package org.server.game_logic;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.lib.data_structures.payloads.Coordinates;
import org.lib.data_structures.payloads.actors.Actor;
import org.lib.data_structures.payloads.GameState;
import org.lib.data_structures.payloads.PlayerInput;
import org.lib.data_structures.payloads.actors.Bullet;

import java.util.ArrayList;
import java.util.List;

public class GameStateService {
    private final List<Actor> actors = new ArrayList<>();
    // private final Object lock = new Object();

    public synchronized void updateGameStateByInput(PlayerInput input) {
        var actor = getActorByClientUUID(input.getClientUUID());
        KeyBindingsHandler.handleInput(input.getKeyInputCode(), actor, actors, input);
    }

    public synchronized void updateGameThread() {
        List<Actor> toRemove = new ArrayList<>();

        for (Actor actor : actors) {
            if (actor instanceof Bullet bullet) {
                Vector2D current = new Vector2D(bullet.getCoordinates().getX(), bullet.getCoordinates().getY());
                Vector2D target = new Vector2D(bullet.getTargetCoordinates().getX(), bullet.getTargetCoordinates().getY());

                Vector2D direction = target.subtract(current).normalize();
                Vector2D movement = direction.scalarMultiply(bullet.getMovementSpeed());
                Vector2D newPos = current.add(movement);

                if (current.distance(target) <= bullet.getMovementSpeed()) {
                    toRemove.add(bullet);
                } else {
                    bullet.setCoordinates(new Coordinates((int)newPos.getX(), (int)newPos.getY()));
                }
            }
        }

        actors.removeAll(toRemove);
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

    private Actor getActorByClientUUID(String clientUUID) {
        return actors.stream()
                .filter(a -> a.getClientUUID().equals(clientUUID))
                .findFirst()
                .orElseThrow();
    }
}

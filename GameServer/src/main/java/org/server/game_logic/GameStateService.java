package org.server.game_logic;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.lib.data_structures.payloads.Coordinates;
import org.lib.data_structures.payloads.DirectionVector;
import org.lib.data_structures.payloads.actors.Actor;
import org.lib.data_structures.payloads.GameState;
import org.lib.data_structures.payloads.PlayerInput;
import org.lib.data_structures.payloads.actors.Bullet;

import java.util.ArrayList;
import java.util.List;

public class GameStateService {
    private final List<Actor> actors = new ArrayList<>();

    public synchronized void updateGameStateByInput(PlayerInput input) {
        var actor = getActorByClientUUID(input.getClientUUID());
        KeyBindingsHandler.handleInput(input.getKeyInputCode(), actor, actors, input);
    }

    public synchronized void updateGameThread() {
        List<Actor> toRemove = new ArrayList<>();

        for (Actor actor : actors) {
            if (actor instanceof Bullet bullet) {
                long currentTime = System.currentTimeMillis();

                if (currentTime - bullet.getCreationTime() >= bullet.getLifespan()) {
                    toRemove.add(bullet);
                    continue;
                }

                Vector2D current = new Vector2D(bullet.getCoordinates().getX(), bullet.getCoordinates().getY());
                Vector2D direction = DirectionVector.toVector2D(bullet.getDirection());
                if (direction.getNorm() == 0) continue;

                Vector2D movement = direction.normalize().scalarMultiply(bullet.getMovementSpeed());
                Vector2D newPos = current.add(movement);

                bullet.setCoordinates(new Coordinates((int) newPos.getX(), (int) newPos.getY()));
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

    public Actor getActorByClientUUID(String clientUUID) {
        return actors.stream()
                .filter(a -> a.getClientUUID().equals(clientUUID))
                .findFirst()
                .orElseThrow();
    }

    public void removeActor(String clientUUID) {
        var actor = getActorByClientUUID(clientUUID);
        actors.remove(actor);
    }
}

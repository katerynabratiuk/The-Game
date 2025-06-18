package org.server.game_logic;

import org.lib.data_structures.payloads.Actor;
import org.lib.data_structures.payloads.GameState;
import org.lib.data_structures.payloads.PlayerInput;

import java.util.ArrayList;
import java.util.List;

public class GameStateService {
    private final List<Actor> actors = new ArrayList<>();

    public synchronized void updateActorByInput(PlayerInput input) {
        var actor = getActorByClientUUID(input.getClientUUID());
        PositionUpdater.handleInput(input.getKeyInputCode(), actor);
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

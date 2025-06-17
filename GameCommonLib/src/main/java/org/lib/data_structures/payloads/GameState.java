package org.lib.data_structures.payloads;

import java.util.ArrayList;
import java.util.List;

public class GameState extends Payload {
    private List<Actor> actors;

    public GameState() {};
    public GameState(ArrayList<Actor> actors) {
        this.actors = actors;
    }

    public void updateActor(String uuid, Coordinates coordinates) {
        boolean updated = false;

        for (int i = 0; i < actors.size(); i++) {
            Actor actor = actors.get(i);
            if (actor.getClientId().equals(uuid)) {
                actors.set(i, new Actor(uuid, coordinates));
                updated = true;
                break;
            }
        }

        if (!updated) {
            actors.add(new Actor(uuid, coordinates));
        }
    }

    public List<Actor> getActorsSnapshot() {
        return new ArrayList<>(actors);
    }

    public void removeActor(String uuid) {
        actors.removeIf(actor -> actor.getClientId().equals(uuid));
    }
}
package org.lib.data_structures.payloads;

import lombok.NoArgsConstructor;
import org.lib.data_structures.payloads.actors.Actor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class GameState extends Payload {
    private List<Actor> actors;

    public GameState(ArrayList<Actor> actors) {
        this.actors = actors;
    }

    public void updateActor(String clientUUID, Coordinates coordinates) {
        // TODO: check this method logic
        boolean updated = false;

        for (int i = 0; i < actors.size(); i++) {
            Actor actor = actors.get(i);
            if (actor.getClientUUID().equals(clientUUID)) {
                actors.set(i, new Actor(coordinates, clientUUID));
                updated = true;
                break;
            }
        }

        if (!updated) {
            actors.add(new Actor(coordinates, clientUUID));
        }
    }

    public List<Actor> getActorsSnapshot() {
        return new ArrayList<>(actors);
    }

    public void setActorsSnapshot(List<Actor> actorsSnapshot) {
        this.actors = actorsSnapshot;
    }

    public void removeActor(String uuid) {
        actors.removeIf(actor -> actor.getUuid().equals(uuid));
    }
}
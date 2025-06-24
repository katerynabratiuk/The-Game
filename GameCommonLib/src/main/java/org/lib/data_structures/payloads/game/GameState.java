package org.lib.data_structures.payloads.game;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data_structures.payloads.Payload;
import org.lib.data_structures.payloads.actors.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class GameState extends Payload {
    private List<Actor> actors;

    @Getter @Setter
    private Map<String, Integer> playerKills;

    public GameState(ArrayList<Actor> actors, Map<String, Integer> playerKills) {
        this.actors = actors;
        this.playerKills = playerKills;
    }

    public List<Actor> getActorsSnapshot() {
        return new ArrayList<>(actors);
    }

    public void setActorsSnapshot(List<Actor> actorsSnapshot) {
        this.actors = actorsSnapshot;
    }
}
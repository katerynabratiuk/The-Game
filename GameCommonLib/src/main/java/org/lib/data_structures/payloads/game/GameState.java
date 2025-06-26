package org.lib.data_structures.payloads.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data_structures.payloads.Payload;
import org.lib.data_structures.payloads.actors.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class GameState extends Payload {
    @Getter @Setter
    private List<Actor> actors;

    @Getter @Setter
    private Map<String, Integer> playerKills;
}
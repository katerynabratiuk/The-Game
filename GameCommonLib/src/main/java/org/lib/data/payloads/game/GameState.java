package org.lib.data.payloads.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data.payloads.Payload;
import org.lib.data.payloads.actors.Actor;

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
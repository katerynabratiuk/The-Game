package org.server.game_logic;

import org.lib.data_structures.payloads.PlayerInput;
import org.lib.data_structures.payloads.actors.Actor;

import java.util.List;

@FunctionalInterface
public interface InputAction {
    void apply(Actor actor, List<Actor> actors, PlayerInput input);
}

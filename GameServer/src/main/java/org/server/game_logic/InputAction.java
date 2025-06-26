package org.server.game_logic;

import org.lib.data.payloads.game.PlayerInput;
import org.lib.data.payloads.actors.Actor;

import java.util.List;

@FunctionalInterface
public interface InputAction {
    void apply(Actor actor, List<Actor> actors, PlayerInput input);
}

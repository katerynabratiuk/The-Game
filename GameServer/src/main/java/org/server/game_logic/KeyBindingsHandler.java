package org.server.game_logic;

import org.lib.data_structures.payloads.Vector;
import org.lib.data_structures.payloads.PlayerInput;
import org.lib.data_structures.payloads.actors.Actor;
import org.lib.data_structures.payloads.Coordinates;
import org.lib.data_structures.payloads.actors.Bullet;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyBindingsHandler {
    private static final Map<Integer, InputAction> inputActions = new HashMap<>();

    static {
        inputActions.put(KeyEvent.VK_LEFT, (actor, actors, input) -> move(actor, -1, 0));
        inputActions.put(KeyEvent.VK_A, (actor, actors, input) -> move(actor, -1, 0));

        inputActions.put(KeyEvent.VK_RIGHT, (actor, actors, input) -> move(actor, 1, 0));
        inputActions.put(KeyEvent.VK_D, (actor, actors, input) -> move(actor, 1, 0));

        inputActions.put(KeyEvent.VK_UP, (actor, actors, input) -> move(actor, 0, -1));
        inputActions.put(KeyEvent.VK_W, (actor, actors, input) -> move(actor, 0, -1));

        inputActions.put(KeyEvent.VK_DOWN, (actor, actors, input) -> move(actor, 0, 1));
        inputActions.put(KeyEvent.VK_S, (actor, actors, input) -> move(actor, 0, 1));

        inputActions.put(MouseEvent.BUTTON1, KeyBindingsHandler::shoot);
    }

    public static void handleInput(int keyCode, Actor actor, List<Actor> actors, PlayerInput input) {
        inputActions.getOrDefault(keyCode, (a, l, i) -> {}).apply(actor, actors, input);
    }

    // idea: send to server player`s direction like it is done with bullets
    private static void move(Actor actor, int dx, int dy) {
        Coordinates coord = actor.getCoordinates();
        Coordinates newCoord = new Coordinates(coord.getX() + dx, coord.getY() + dy);
        actor.setCoordinates(newCoord);
    }

    private static void shoot(Actor actor, List<Actor> actors, PlayerInput input) {
        double x = input.getDirection().getX();
        double y = input.getDirection().getY();
        var newActor = new Bullet(
                input.getClientUUID(),
                new Coordinates(actor.getCoordinates().getX() - 1, actor.getCoordinates().getY() - 1),
                new Vector(x, y));

        actors.add(newActor);
    }
}

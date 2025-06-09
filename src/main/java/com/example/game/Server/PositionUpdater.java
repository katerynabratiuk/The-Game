package com.example.game.Server;

import com.example.game.DataStructures.Actor;
import com.example.game.DataStructures.Coordinates;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class PositionUpdater {
    private static final Map<Integer, Function<Actor, Actor>> inputActions = new HashMap<>();

    static {
        inputActions.put(KeyEvent.VK_LEFT, actor -> move(actor, -1, 0));
        inputActions.put(KeyEvent.VK_A, actor -> move(actor, -1, 0));

        inputActions.put(KeyEvent.VK_RIGHT, actor -> move(actor, 1, 0));
        inputActions.put(KeyEvent.VK_D, actor -> move(actor, 1, 0));

        inputActions.put(KeyEvent.VK_UP, actor -> move(actor, 0, -1));
        inputActions.put(KeyEvent.VK_W, actor -> move(actor, 0, -1));

        inputActions.put(KeyEvent.VK_DOWN, actor -> move(actor, 0, 1));
        inputActions.put(KeyEvent.VK_S, actor -> move(actor, 0, 1));

        inputActions.put(KeyEvent.VK_SPACE, actor -> turn(actor));
    }

    public static Coordinates calculateNewPosition(int keyCode, Coordinates coords) {
        if (keyCode == KeyEvent.VK_LEFT) {
            return new Coordinates(coords.x() - 1, coords.y());
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            return new Coordinates(coords.x() + 1, coords.y());
        } else if (keyCode == KeyEvent.VK_UP) {
            return new Coordinates(coords.x(), coords.y() - 1);
        } else if (keyCode == KeyEvent.VK_DOWN) {
            return new Coordinates(coords.x(), coords.y() + 1);
        } else {
            return coords;
        }
    }

    public static Actor handleInput(int keyCode, Actor actor) {
        return inputActions.getOrDefault(keyCode, a -> a).apply(actor);
    }

    private static Actor move(Actor actor, int dx, int dy) {
        Coordinates coord = actor.getCoordinates();
        Coordinates newCoord = new Coordinates(coord.x() + dx, coord.y() + dy);
        actor.setCoordinates(newCoord);
        return actor;
    }

    private static Actor turn(Actor actor) {
        System.out.println("Around the world with " + actor.getClientId());
        return actor;
    }
}

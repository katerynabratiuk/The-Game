package org.server.game_logic;

import org.lib.data_structures.payloads.actors.PlayerCharacter;
import org.lib.data_structures.payloads.game.Vector;
import org.lib.data_structures.payloads.game.PlayerInput;
import org.lib.data_structures.payloads.actors.Actor;
import org.lib.data_structures.payloads.actors.Coordinates;
import org.lib.data_structures.payloads.actors.Bullet;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class KeyBindingsHandler {
    private static final Set<Integer> MOVEMENT_KEYS = Set.of(
            KeyEvent.VK_LEFT, KeyEvent.VK_A,
            KeyEvent.VK_RIGHT, KeyEvent.VK_D,
            KeyEvent.VK_UP, KeyEvent.VK_W,
            KeyEvent.VK_DOWN, KeyEvent.VK_S
    );

    private static final Map<String, Map<Integer, Boolean>> clientKeyStates = new ConcurrentHashMap<>();
    private static final Map<Integer, InputAction> inputActions = new HashMap<>();

    static {
        inputActions.put(MouseEvent.BUTTON1, KeyBindingsHandler::shoot);
    }

    public static void updateKeyState(String clientUUID, int keyCode, boolean isPressed) {
        if (!MOVEMENT_KEYS.contains(keyCode)) return;

        clientKeyStates.computeIfAbsent(clientUUID, k -> new HashMap<>());
        clientKeyStates.get(clientUUID).put(keyCode, isPressed);
    }

    public static void processInput(Actor actor, List<Actor> actors, PlayerInput input) {
        if (input == null || actor == null) return;

        // apply actions like shooting
        if (inputActions.containsKey(input.getKeyInputCode())) {
            inputActions.get(input.getKeyInputCode()).apply(actor, actors, input);
            return;
        }

        // apply movement
        calculateAndApplyMovement(actor, input.getClientUUID());
    }

    private static void calculateAndApplyMovement(Actor actor, String clientUUID) {
        if (!clientKeyStates.containsKey(clientUUID)) return;

        Map<Integer, Boolean> states = clientKeyStates.get(clientUUID);
        double dx = 0;
        double dy = 0;
        double speed = actor.getMovementSpeed();

        if (states.getOrDefault(KeyEvent.VK_LEFT, false) || states.getOrDefault(KeyEvent.VK_A, false)) dx -= 1;
        if (states.getOrDefault(KeyEvent.VK_RIGHT, false) || states.getOrDefault(KeyEvent.VK_D, false)) dx += 1;
        if (states.getOrDefault(KeyEvent.VK_UP, false) || states.getOrDefault(KeyEvent.VK_W, false)) dy -= 1;
        if (states.getOrDefault(KeyEvent.VK_DOWN, false) || states.getOrDefault(KeyEvent.VK_S, false)) dy += 1;

        if (dx != 0 && dy != 0) {
            double length = Math.sqrt(dx * dx + dy * dy);
            dx /= length;
            dy /= length;
        }

        if (dx != 0 || dy != 0) {
            dx *= speed;
            dy *= speed;
            move(actor, (int)Math.round(dx), (int)Math.round(dy));
        }
    }

    private static void move(Actor actor, int dx, int dy) {
        var current = actor.getCoordinates();
        var newCoord = new Coordinates((current.getX() + dx), (current.getY() + dy));
        actor.setCoordinates(newCoord);
    }

    private static void shoot(Actor actor, List<Actor> actors, PlayerInput input) {
        var player = (PlayerCharacter) actor;
        if(!player.determineAttackReady()) return;

        double x = input.getDirection().getX();
        double y = input.getDirection().getY();

        var newActor = new Bullet(
                input.getClientUUID(),
                new Coordinates(player.getCoordinates().getX(), player.getCoordinates().getY()),
                new Vector(x, y),
                player.getDamage()
        );
        actors.add(newActor);
        player.setLastAttackTime(System.currentTimeMillis());
    }
}
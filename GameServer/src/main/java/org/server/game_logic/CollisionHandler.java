package org.server.game_logic;

import org.lib.data_structures.payloads.actors.Actor;

import java.util.List;
import java.util.function.Consumer;

public class CollisionHandler {
    public void checkCollisions(List<Actor> actors, Consumer<Actor> onKill, Consumer<Actor> onDestroy) {
        for (Actor trigger : actors) {
            for (Actor target : actors) {
                if (target == trigger) continue;
                if (isCollision(trigger, target)) {
                    trigger.OnCollision(target);
                    if (trigger.isKilled()) {
                        onKill.accept(trigger);
                    }
                }
            }

            if (trigger.isPendingDestroy()) {
                onDestroy.accept(trigger);
            }
        }
    }

    private boolean isCollision(Actor a, Actor b) {
        double dx = a.getCoordinates().getX() - b.getCoordinates().getX();
        double dy = a.getCoordinates().getY() - b.getCoordinates().getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance <= (a.getRadius() + b.getRadius());
    }
}
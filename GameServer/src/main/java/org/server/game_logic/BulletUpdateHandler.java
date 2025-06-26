package org.server.game_logic;

import org.lib.data.payloads.actors.Actor;
import org.lib.data.payloads.actors.Bullet;

import java.util.ArrayList;
import java.util.List;

public class BulletUpdateHandler {
    public List<Actor> updateAndCleanupBullets(List<Actor> actors) {
        List<Actor> toRemove = new ArrayList<>();
        for (Actor actor : actors) {
            if (actor instanceof Bullet bullet) {
                bullet.onNextFrame();
                if (bullet.isPendingDestroy()) {
                    toRemove.add(bullet);
                }
            }
        }
        return toRemove;
    }
}
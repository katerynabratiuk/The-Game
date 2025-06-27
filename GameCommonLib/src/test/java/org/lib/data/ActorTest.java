package org.lib.data;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.lib.data.payloads.actors.Bullet;
import org.lib.data.payloads.actors.Coordinates;
import org.lib.data.payloads.actors.PlayerCharacter;
import org.lib.data.payloads.game.Vector;


public class ActorTest {
    @Test
    public void testBulletExpiresAfterLifespan() throws InterruptedException {
        var bullet = new Bullet();
        bullet.setLifespan(100);

        Thread.sleep(150);
        bullet.onNextFrame();

        assertTrue(bullet.isPendingDestroy());
    }

    @Test
    public void testBulletMovesInDirection() {
        var start = new Coordinates(0, 0);
        var direction = new Vector(1, 0);

        var bullet = new Bullet("client", start, direction, 1.0, 1);
        bullet.setMovementSpeed(10.0);
        bullet.onNextFrame();

        var newPos = bullet.getCoordinates();
        assertEquals(10, newPos.getX());
        assertEquals(0, newPos.getY());
    }

    @Test
    public void testBulletOnCollisionWithSameClientDoesNotDestroy() {
        var bullet = new Bullet();
        bullet.setClientUUID("test");

        var target = new PlayerCharacter();
        target.setClientUUID("test");

        bullet.OnCollision(target);
        assertFalse(bullet.isPendingDestroy());
    }

    @Test
    public void testBulletOnCollisionWithDifferentClientSetsDestroy() {
        var bullet = new Bullet();
        bullet.setClientUUID("test");

        var target = new PlayerCharacter();
        target.setClientUUID("test2");

        bullet.OnCollision(target);
        assertTrue(bullet.isPendingDestroy());
    }

    @Test
    public void testCollisionWithBulletReducesHitPoints() {
        var player = new PlayerCharacter("uuid", new Coordinates(0, 0), "Player");
        int initialHp = player.getHitPoints();

        var bullet = new Bullet();
        bullet.setDamage(2);
        bullet.setClientUUID("enemy");

        player.OnCollision(bullet);
        assertEquals(initialHp - 2, player.getHitPoints());
    }

    @Test
    public void testCollisionWithOwnBulletDoesNotDamage() {
        PlayerCharacter player = new PlayerCharacter("uuid", new Coordinates(0, 0), "Player");

        var bullet = new Bullet();
        bullet.setDamage(2);
        bullet.setClientUUID("uuid");

        player.OnCollision(bullet);
        assertEquals(30, player.getHitPoints());
    }

    @Test
    public void testCharacterDiesWhenHpZero() {
        var player = new PlayerCharacter();
        player.setHitPoints(1);

        var bullet = new Bullet();
        bullet.setDamage(1);
        bullet.setClientUUID("test");

        player.OnCollision(bullet);

        assertTrue(player.isPendingDestroy());
        assertTrue(player.isKilled());
        assertEquals(0, player.getHitPoints());
    }

    @Test
    public void testDetermineAttackReady() throws InterruptedException {
        var player = new PlayerCharacter();
        player.setRateOfFire(200);
        player.setLastAttackTime(System.currentTimeMillis());

        assertFalse(player.determineAttackReady());
        Thread.sleep(250);
        assertTrue(player.determineAttackReady());
    }
}

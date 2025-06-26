package org.lib.data.payloads.actors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerCharacter extends Actor {
    private String username;

    private int maxHp = 5;
    private int hitPoints = 5;

    private Double sprayAngle = 0.0;
    private double rateOfFire = 1000;
    private double damage = 1;
    private long lastAttackTime = 0;

    private Inventory inventory;


    public PlayerCharacter(String clientUUID, Coordinates coordinates, String username) {
        setRadius(20);
        setMovementSpeed(20);
        updateColor(Color.BLUE);
        setCoordinates(coordinates);
        setClientUUID(clientUUID);
        this.username = username;
    }

    @Override
    public void OnCollision(Actor target) {
        if (Objects.equals(getClientUUID(), target.getClientUUID())) return; // prevent self-damage from bullets

        if (target instanceof Bullet bullet) {
            takeDamage(bullet.getDamage());
        }
    }

    public double calcCooldownRatio() {
        long elapsed = System.currentTimeMillis() - lastAttackTime;
        return Math.min((double) elapsed / rateOfFire, 1.0);
    }

    public boolean determineAttackReady() {
        return System.currentTimeMillis() - lastAttackTime >= rateOfFire;
    }

    private void takeDamage(int damage) {
        int newHp = getHitPoints() - damage;
        updateHitPoints(newHp);
    }

    private void updateHitPoints(int newHp) {
        if (newHp <= 0) {
            setPendingDestroy(true);
            setKilled(true);
        }
        setHitPoints(newHp);
    }
}

package org.lib.data_structures.payloads.actors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class PlayerCharacter extends Actor {
    @Getter @Setter
    private int hitPoints = 5;

    @Getter @Setter
    private String username;

    @Getter
    private int maxHp = 5;

    @JsonIgnore @Getter @Setter
    private double movementSpeed = 10;

    @JsonIgnore @Getter @Setter
    private double sprayAngle = 0;

    @JsonIgnore @Getter @Setter
    private double rateOfFire = 0;

    public PlayerCharacter(String clientUUID, Coordinates coordinates, String username) {
        setRadius(10);
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

package org.lib.data_structures.payloads.actors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.lib.data_structures.payloads.game.Vector;

import java.awt.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class Bullet extends Actor {
    @JsonIgnore @Getter @Setter
    private double movementSpeed = 5.0;

    @JsonIgnore @Getter @Setter
    private int lifespan = 500; // in milliseconds

    @JsonIgnore @Getter @Setter
    private int damage = 1;

    @JsonIgnore @Getter @Setter
    private Vector direction;

    @JsonIgnore @Getter @Setter
    private long creationTime = System.currentTimeMillis();

    public Bullet(String clientUUID, Coordinates coordinates, Vector vector) {
        setCoordinates(coordinates);
        setClientUUID(clientUUID);
        setRadius(5);
        updateColor(Color.RED);
        this.creationTime = System.currentTimeMillis();
        this.direction = vector;
    }

    @Override
    public void OnCollision(Actor target) {
        if (Objects.equals(getClientUUID(), target.getClientUUID())) return;
        setPendingDestroy(true);
    }

    public void onNextFrame() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - creationTime  >= lifespan) {
            setPendingDestroy(true);
            return;
        }

        var current = new Vector2D(getCoordinates().getX(), getCoordinates().getY());
        var direction = Vector.toVector2D(this.direction);
        if (direction.getNorm() == 0) return;

        var movement = direction.normalize().scalarMultiply(movementSpeed);
        var newPos = current.add(movement);

        setCoordinates(new Coordinates((int) newPos.getX(), (int) newPos.getY()));
    }
}

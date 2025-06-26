package org.lib.data.payloads.game;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.lib.data.payloads.Payload;
import org.lib.data.payloads.actors.Coordinates;

@Getter
@NoArgsConstructor
public class Vector extends Payload {
    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x  = x;
        this.y = y;
    }

    public Vector(Coordinates coordinates) {
        this.x  = coordinates.getX();
        this.y = coordinates.getY();
    }

    public static Vector2D toVector2D(Vector direction) {
        return new Vector2D(direction.getX(), direction.getY());
    }
}

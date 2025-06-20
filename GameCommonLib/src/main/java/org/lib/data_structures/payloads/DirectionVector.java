package org.lib.data_structures.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

@Getter
@NoArgsConstructor
public class DirectionVector extends Payload {
    private double x;
    private double y;

    public DirectionVector(double x, double y) {
        this.x  = x;
        this.y = y;
    }

    public DirectionVector(Coordinates coordinates) {
        this.x  = coordinates.getX();
        this.y = coordinates.getY();
    }

    public static Vector2D toVector2D(DirectionVector direction) {
        return new Vector2D(direction.getX(), direction.getY());
    }
}

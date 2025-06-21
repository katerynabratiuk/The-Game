package org.lib.data_structures.payloads.actors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data_structures.payloads.Coordinates;
import org.lib.data_structures.payloads.Vector;

import java.awt.*;

@NoArgsConstructor
@AllArgsConstructor
public class Bullet extends Actor {
    @JsonIgnore @Getter @Setter
    private double movementSpeed = 5.0;

    @JsonIgnore @Getter @Setter
    private int lifespan = 500; // in milliseconds

    @JsonIgnore @Getter @Setter
    private double damage;

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
}

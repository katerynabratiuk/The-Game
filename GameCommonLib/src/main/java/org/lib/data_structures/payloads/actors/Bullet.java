package org.lib.data_structures.payloads.actors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data_structures.payloads.Coordinates;
import org.lib.data_structures.payloads.DirectionVector;

@NoArgsConstructor
@AllArgsConstructor
public class Bullet extends Actor {
    @Getter @Setter private double movementSpeed = 5.0;
    @Getter @Setter private double lifespan = 5000.0; // in milliseconds
    @Getter @Setter private double radius;
    @Getter @Setter private double damage;
    @Getter @Setter private DirectionVector direction;

    public Bullet(Coordinates coordinates, String clientUUID) {
        setCoordinates(coordinates);
        setClientUUID(clientUUID);
    }
}

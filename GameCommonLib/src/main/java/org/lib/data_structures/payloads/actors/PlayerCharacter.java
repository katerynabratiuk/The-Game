package org.lib.data_structures.payloads.actors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data_structures.payloads.Coordinates;

import java.awt.*;

@NoArgsConstructor
@AllArgsConstructor
public class PlayerCharacter extends Actor {
    @Getter @Setter private double hitPoints;
    @Getter @Setter private double movementSpeed = 10; // temp
    @Getter @Setter private double sprayAngle = 0; // temp
    @Getter @Setter private double rateOfFire = 0; // temp in ms

    public PlayerCharacter(String clientUUID, Coordinates coordinates) {
        setRadius(10);
        updateColor(Color.BLUE);
        setCoordinates(coordinates);
        setClientUUID(clientUUID);
    }
}

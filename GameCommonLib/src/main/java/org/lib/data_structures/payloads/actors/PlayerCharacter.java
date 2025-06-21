package org.lib.data_structures.payloads.actors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class PlayerCharacter extends Actor {
    @Getter @Setter private double hitPoints;
    @Getter @Setter private double movementSpeed;
    @Getter @Setter private double sprayAngle;
    @Getter @Setter private double rateOfFire; // in ms

    private PlayerCharacter() {
        setRadius(20);
    }

}

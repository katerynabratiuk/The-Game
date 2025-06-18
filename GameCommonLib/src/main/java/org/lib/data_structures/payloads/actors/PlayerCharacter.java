package org.lib.data_structures.payloads.actors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class PlayerCharacter extends Actor {
    @Getter @Setter private double hitPoints;
    @Getter @Setter private double movementSpeed;
    @Getter @Setter private double sprayAngle;
    @Getter @Setter private double radius; // or other size parameter
    @Getter @Setter private double rateOfFire; // in ms
}

package org.lib.data_structures.payloads.actors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data_structures.payloads.Coordinates;
import org.lib.data_structures.payloads.Payload;

import java.util.UUID;


@NoArgsConstructor
public class Actor extends Payload {
    @Getter private final String uuid = UUID.randomUUID().toString();
    @Getter @Setter private String clientUUID;
    @Getter @Setter private Coordinates coordinates;
    @Getter @Setter private double radius = 10; // circular space occupied by actor

    public Actor(Coordinates coordinates, String clientUUID) {
        this.coordinates = coordinates;
        this.clientUUID = clientUUID;
    }
}

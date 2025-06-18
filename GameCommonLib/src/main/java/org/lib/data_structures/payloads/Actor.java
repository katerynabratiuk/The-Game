package org.lib.data_structures.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@NoArgsConstructor
public class Actor extends Payload {
    @Getter private final String uuid = UUID.randomUUID().toString();
    @Getter @Setter private String clientUUID;
    @Getter @Setter private Coordinates coordinates;

    public Actor(Coordinates coordinates, String clientUUID) {
        this.coordinates = coordinates;
        this.clientUUID = clientUUID;
    }
}

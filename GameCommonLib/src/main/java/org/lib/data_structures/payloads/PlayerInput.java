package org.lib.data_structures.payloads;

import lombok.Getter;
import lombok.Setter;

public class PlayerInput extends Payload {
    @Getter private String clientUUID;
    @Getter private int keyInputCode;
    @Getter @Setter private DirectionVector direction;

    public PlayerInput() {}

    public PlayerInput(String clientUUID, int keyInputCode) {
        this.clientUUID = clientUUID;
        this.keyInputCode = keyInputCode;
    }
}

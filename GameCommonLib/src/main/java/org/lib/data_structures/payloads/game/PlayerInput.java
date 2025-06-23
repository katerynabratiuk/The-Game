package org.lib.data_structures.payloads.game;

import lombok.Getter;
import lombok.Setter;
import org.lib.data_structures.payloads.Payload;

public class PlayerInput extends Payload {
    @Getter private String clientUUID;
    @Getter private int keyInputCode;
    @Getter @Setter private Vector direction;

    public PlayerInput() {}

    public PlayerInput(String clientUUID, int keyInputCode) {
        this.clientUUID = clientUUID;
        this.keyInputCode = keyInputCode;
    }
}

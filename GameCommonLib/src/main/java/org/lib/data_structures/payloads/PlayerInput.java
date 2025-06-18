package org.lib.data_structures.payloads;

import lombok.Getter;

public class PlayerInput extends Payload {
    @Getter private String clientUUID;
    @Getter private int keyInputCode;

    public PlayerInput() {}

    public PlayerInput(String clientUUID, int keyInputCode) {
        this.clientUUID = clientUUID;
        this.keyInputCode = keyInputCode;
    }
}

package org.lib.data_structures.payloads.game;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data_structures.payloads.Payload;

@NoArgsConstructor
public class PlayerInput extends Payload {
    @Getter private int keyInputCode;
    @Getter @Setter private Vector direction;
    @Getter @Setter private boolean keyReleased;

    public PlayerInput(String clientUUID, int keyInputCode) {
        super.setClientUUID(clientUUID);
        this.keyInputCode = keyInputCode;
    }
}

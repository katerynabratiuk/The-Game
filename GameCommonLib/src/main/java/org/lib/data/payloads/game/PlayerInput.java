package org.lib.data.payloads.game;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data.payloads.Payload;

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

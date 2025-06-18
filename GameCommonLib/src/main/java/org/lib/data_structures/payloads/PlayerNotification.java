package org.lib.data_structures.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
public class PlayerNotification extends Payload {
    @Getter @Setter private String clientUUID;
    @Getter @Setter private String message;

    public PlayerNotification(String message) { this.message = message; }
}

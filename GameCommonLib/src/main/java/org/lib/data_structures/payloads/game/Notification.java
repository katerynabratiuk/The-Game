package org.lib.data_structures.payloads.game;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data_structures.payloads.Payload;


@NoArgsConstructor
public class Notification extends Payload {
    @Getter @Setter private String clientUUID;
    @Getter @Setter private String message;

    public Notification(String message) { this.message = message; }
}

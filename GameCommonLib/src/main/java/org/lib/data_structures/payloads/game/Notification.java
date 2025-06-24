package org.lib.data_structures.payloads.game;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data_structures.payloads.Payload;
import org.lib.data_structures.payloads.enums.NotificationCode;


@NoArgsConstructor
public class Notification extends Payload {
    @Getter @Setter private String clientUUID;
    @Getter @Setter private String message;
    @Getter @Setter private NotificationCode code;

    public Notification(String message) { this.message = message; }

    public Notification(String message, NotificationCode code) {
        this.message = message;
        this.code = code;
    }

}

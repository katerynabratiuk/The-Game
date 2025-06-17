package org.lib.DataStructures.payloads;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static org.lib.DataStructures.payloads.PayloadStructType.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlayerInput.class, name = "PLAYER_INPUT"),
        @JsonSubTypes.Type(value = GameState.class, name = "GAME_STATE"),
        @JsonSubTypes.Type(value = Actor.class, name = "ACTOR"),
        @JsonSubTypes.Type(value = PlayerNotification.class, name = "PLAYER_NOTIFICATION")
})
public class Payload {
    public PayloadStructType getType() {
        Class<?> type = this.getClass();
        return switch (type.getSimpleName()) {
            case "PlayerInput" -> PLAYER_INPUT;
            case "GameState" -> GAME_STATE;
            case "Actor" -> ACTOR;
            case "PlayerNotification" -> PLAYER_NOTIFICATION;
            default -> UNKNOWN;
        };
    }
}
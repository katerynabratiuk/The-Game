package org.lib.data_structures.payloads;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.lib.data_structures.payloads.actors.Actor;
import org.lib.data_structures.payloads.actors.Bullet;
import org.lib.data_structures.payloads.actors.PlayerCharacter;

import static org.lib.data_structures.payloads.PayloadStructType.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlayerInput.class, name = "PLAYER_INPUT"),
        @JsonSubTypes.Type(value = GameState.class, name = "GAME_STATE"),
        @JsonSubTypes.Type(value = Actor.class, name = "ACTOR"),
        @JsonSubTypes.Type(value = Bullet.class, name = "BULLET"),
        @JsonSubTypes.Type(value = PlayerCharacter.class, name = "PLAYER_CHARACTER"),
        @JsonSubTypes.Type(value = PlayerNotification.class, name = "PLAYER_NOTIFICATION"),
        @JsonSubTypes.Type(value = ConnectionRequest.class, name = "CONNECTION_REQUEST"),
        @JsonSubTypes.Type(value = ConnectionResponse.class, name = "CONNECTION_RESPONSE")
})
public class Payload {
    @JsonIgnore private String type;

    public Payload() {}

    public PayloadStructType getType() {
        Class<?> type = this.getClass();
        return switch (type.getSimpleName()) {
            case "PlayerInput" -> PLAYER_INPUT;
            case "GameState" -> GAME_STATE;
            case "Actor" -> ACTOR;
            case "PlayerNotification" -> PLAYER_NOTIFICATION;
            case "ConnectionRequest" -> CONNECTION_REQUEST;
            case "ConnectionResponse" -> CONNECTION_RESPONSE;
            default -> UNKNOWN;
        };
    }
}
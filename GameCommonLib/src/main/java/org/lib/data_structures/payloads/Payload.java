package org.lib.data_structures.payloads;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.lib.data_structures.payloads.actors.Actor;
import org.lib.data_structures.payloads.actors.Bullet;
import org.lib.data_structures.payloads.actors.PlayerCharacter;
import org.lib.data_structures.payloads.enums.PayloadStructType;
import org.lib.data_structures.payloads.game.GameState;
import org.lib.data_structures.payloads.game.Notification;
import org.lib.data_structures.payloads.game.PlayerInput;
import org.lib.data_structures.payloads.network.ConnectionRequest;
import org.lib.data_structures.payloads.network.ConnectionResponse;
import org.lib.data_structures.payloads.queries.ClientLogin;


import static org.lib.data_structures.payloads.enums.PayloadStructType.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlayerInput.class, name = "PLAYER_INPUT"),
        @JsonSubTypes.Type(value = GameState.class, name = "GAME_STATE"),
        @JsonSubTypes.Type(value = Actor.class, name = "ACTOR"),
        @JsonSubTypes.Type(value = Bullet.class, name = "BULLET"),
        @JsonSubTypes.Type(value = PlayerCharacter.class, name = "PLAYER_CHARACTER"),
        @JsonSubTypes.Type(value = Notification.class, name = "PLAYER_NOTIFICATION"),
        @JsonSubTypes.Type(value = ConnectionRequest.class, name = "CONNECTION_REQUEST"),
        @JsonSubTypes.Type(value = ConnectionResponse.class, name = "CONNECTION_RESPONSE"),
        @JsonSubTypes.Type(value = ClientLogin.class, name = "CLIENT_QUERY") // MOCK
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
            case "Notification" -> NOTIFICATION;
            case "ConnectionRequest" -> CONNECTION_REQUEST;
            case "ConnectionResponse" -> CONNECTION_RESPONSE;
            case "ClientLogin" -> CLIENT_QUERY;
            default -> UNKNOWN;
        };
    }
}
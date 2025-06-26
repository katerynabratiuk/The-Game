package org.lib.data.payloads;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import org.lib.data.payloads.actors.Actor;
import org.lib.data.payloads.actors.Bullet;
import org.lib.data.payloads.actors.Inventory;
import org.lib.data.payloads.actors.PlayerCharacter;
import org.lib.data.payloads.enums.PayloadStructType;
import org.lib.data.payloads.game.GameState;
import org.lib.data.payloads.game.Notification;
import org.lib.data.payloads.game.PlayerInput;
import org.lib.data.payloads.network.ConnectionRequest;
import org.lib.data.payloads.network.ConnectionResponse;
import org.lib.data.payloads.queries.*;
import org.lib.data.payloads.queries.search.CharacterFilterPayload;


import static org.lib.data.payloads.enums.PayloadStructType.*;

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
        @JsonSubTypes.Type(value = RegisterPayload.class, name = "REGISTER"),
        @JsonSubTypes.Type(value = LoginPayload.class, name = "LOGIN"),
        @JsonSubTypes.Type(value = UserPickPayload.class, name = "PICK"),
        @JsonSubTypes.Type(value = CharacterListPayload.class, name = "CHARACTER_LIST"),
        @JsonSubTypes.Type(value = CharacterFilterPayload.class, name = "SEARCH_CHARACTER"),
        @JsonSubTypes.Type(value = WeaponListPayload.class, name = "WEAPON_LIST"),
        @JsonSubTypes.Type(value = PowerUpListPayload.class, name = "POWERUP_LIST"),
        @JsonSubTypes.Type(value = Inventory.class, name = "INVENTORY")
})
public class Payload {
    @JsonIgnore private String type;

    @Getter @Setter
    private String clientUUID;

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
            case "RegisterPayload" -> REGISTER;
            case "LoginPayload" -> LOGIN;
            case "UserPickPayload" -> PICK;
            case "Inventory" -> INVENTORY;
            default -> UNKNOWN;
        };
    }
}
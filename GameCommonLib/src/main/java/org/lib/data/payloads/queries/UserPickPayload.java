package org.lib.data.payloads.queries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data.payloads.Payload;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserPickPayload extends Payload {
    private Integer characterId;
    private Integer weaponId;
    private Integer powerUpId;

    public UserPickPayload(Integer characterId, Integer weaponId, Integer powerUpId, String clientUUID) {
        this.characterId = characterId;
        this.weaponId = weaponId;
        this.powerUpId = powerUpId;
        super.setClientUUID(clientUUID);
    }
}
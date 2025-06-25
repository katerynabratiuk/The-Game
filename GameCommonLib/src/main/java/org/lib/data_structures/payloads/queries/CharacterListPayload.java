package org.lib.data_structures.payloads.queries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data_structures.payloads.Payload;
import org.lib.data_structures.payloads.enums.PayloadStructType;
import org.server.db.dto.CharacterDTO;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CharacterListPayload extends Payload {
    private List<CharacterDTO> characters;

    public CharacterListPayload(List<CharacterDTO> characters, String ClientUUID) {
        this.characters = characters;
        super.setClientUUID(ClientUUID);
    }

    @Override
    public PayloadStructType getType() {
        return PayloadStructType.CHARACTER_LIST;
    }

}


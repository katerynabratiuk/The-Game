package org.lib.data.payloads.queries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data.dto.ItemDTO;
import org.lib.data.payloads.Payload;
import org.lib.data.payloads.enums.PayloadStructType;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WeaponListPayload extends Payload {

    private List<ItemDTO> itemList;

    @Override
    public PayloadStructType getType() {
        return PayloadStructType.WEAPON_LIST;
    }
}

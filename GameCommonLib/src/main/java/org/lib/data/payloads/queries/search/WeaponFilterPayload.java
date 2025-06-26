package org.lib.data.payloads.queries.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data.payloads.Payload;
import org.lib.data.payloads.enums.PayloadStructType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeaponFilterPayload extends Payload {
    private String name;
    private List<SortField> sortBy;

    public enum SortField {
        DAMAGE,
        SPRAY,
        RATE_OF_FIRE
    }

    @Override
    public PayloadStructType getType() {
        return PayloadStructType.WEAPON_FILTER;
    }
}

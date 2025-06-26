package org.lib.data.payloads.queries.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data.payloads.Payload;
import org.lib.data.payloads.enums.PayloadStructType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CharacterFilterPayload extends Payload {
    String name;
    Boolean fast;
    Boolean armor;


    @Override
    public PayloadStructType getType() {
        return PayloadStructType.CHARACTER_FILTER;
    }


}

package org.lib.data_structures.payloads.queries;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.lib.data_structures.payloads.Payload;
import org.lib.data_structures.payloads.enums.PayloadStructType;

@Getter
@AllArgsConstructor
public class RegisterPayload extends Payload {
    private final String username;
    private final String password;

    @Override
    public PayloadStructType getType() {
        return PayloadStructType.REGISTER;
    }
}

package org.lib.data_structures.payloads.queries;

import lombok.Getter;
import org.lib.data_structures.payloads.Payload;
import org.lib.data_structures.payloads.enums.PayloadStructType;

@Getter
public class LoginPayload extends Payload {
    private final String username;
    private final String password;

    public LoginPayload(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public PayloadStructType getType() {
        return PayloadStructType.LOGIN;
    }

}


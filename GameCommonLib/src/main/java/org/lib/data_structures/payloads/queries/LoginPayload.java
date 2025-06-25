package org.lib.data_structures.payloads.queries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lib.data_structures.payloads.Payload;
import org.lib.data_structures.payloads.enums.PayloadStructType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginPayload extends Payload {
    private String username;
    private String password;

    public LoginPayload(String username, String password, String clientUUID) {
        this.username = username;
        this.password = password;
        super.setClientUUID(clientUUID);
    }


    @Override
    public PayloadStructType getType() {
        return PayloadStructType.LOGIN;
    }

}


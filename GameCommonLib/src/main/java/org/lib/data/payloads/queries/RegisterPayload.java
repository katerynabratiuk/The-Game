package org.lib.data.payloads.queries;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lib.data.payloads.Payload;
import org.lib.data.payloads.enums.PayloadStructType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPayload extends Payload {
    private String username;
    private String password;

    public RegisterPayload(String username, String password, String clientUUID) {
        this.username = username;
        this.password = password;
        super.setClientUUID(clientUUID);
    }

    @Override
    public PayloadStructType getType() {
        return PayloadStructType.REGISTER;
    }
}

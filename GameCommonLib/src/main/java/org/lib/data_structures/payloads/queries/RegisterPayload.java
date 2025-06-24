package org.lib.data_structures.payloads.queries;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data_structures.payloads.Payload;
import org.lib.data_structures.payloads.enums.PayloadStructType;

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

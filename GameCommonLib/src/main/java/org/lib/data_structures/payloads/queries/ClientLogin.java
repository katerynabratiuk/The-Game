package org.lib.data_structures.payloads.queries;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lib.data_structures.payloads.Payload;

@Getter
@NoArgsConstructor
public class ClientLogin extends Payload {
    // mocking real client query
    private String clientUUID;
    private String username;

    public ClientLogin(String clientId, String username) {
        this.clientUUID = clientId;
        this.username = username;
    }
}

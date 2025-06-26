package org.lib.data.payloads.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data.payloads.Payload;
import org.lib.data.payloads.enums.ConnectionCode;

@AllArgsConstructor
@NoArgsConstructor
public class ConnectionRequest extends Payload {
    @Getter @Setter private String clientUUID;
    @Getter @Setter private ConnectionCode connectionCode;
}

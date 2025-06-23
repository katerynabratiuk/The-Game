package org.lib.data_structures.payloads.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data_structures.payloads.Payload;
import org.lib.data_structures.payloads.enums.ConnectionCode;

@AllArgsConstructor
@NoArgsConstructor
public class ConnectionRequest extends Payload {
    @Getter @Setter private String clientUUID;
    @Getter @Setter private ConnectionCode connectionCode;
}

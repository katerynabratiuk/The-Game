package org.lib.data_structures.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class ConnectionRequest extends Payload{
    @Getter @Setter private String clientUUID;
    @Getter @Setter private ConnectionCode connectionCode;
}

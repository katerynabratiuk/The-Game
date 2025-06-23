package org.lib.data_structures.payloads.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data_structures.payloads.Payload;

@AllArgsConstructor
@NoArgsConstructor
public class ConnectionResponse extends Payload {
    @Getter @Setter private String clientUUID;
}

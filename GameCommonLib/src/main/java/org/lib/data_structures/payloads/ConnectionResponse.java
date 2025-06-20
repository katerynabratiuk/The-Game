package org.lib.data_structures.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class ConnectionResponse extends Payload{
    @Getter @Setter private String clientUUID;
}

package org.lib.data_structures.payloads;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class NetworkPayload {
    @Getter @Setter
    private List<Payload> payloads;

    public NetworkPayload() {}

    public NetworkPayload(List<Payload> payloads) {
        this.payloads = payloads;
    }
}


package org.lib.data.payloads;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class NetworkPayload {
    @Getter @Setter private List<Payload> payloads;
    @Getter @Setter private int sequenceNumber;
    @Getter @Setter private String clientUUID;

    public NetworkPayload() {}

    public NetworkPayload(List<Payload> payloads, String clientUUID) {
        this.payloads = payloads;
        this.clientUUID = clientUUID;
    }

    public NetworkPayload(List<Payload> payloads) {
        this.payloads = payloads;
    }
}


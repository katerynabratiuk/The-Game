package org.lib.DataStructures.payloads;

public class PlayerInput extends Payload {
    private final String clientUUID;
    private final int keyInputCode;

    public PlayerInput(String uuid, int keyInputCode) {
        this.clientUUID = uuid;
        this.keyInputCode = keyInputCode;
    }

    public int keyInput() {return this.keyInputCode; }
    public String uuid() {return this.clientUUID; }
}

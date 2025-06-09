package com.example.game.DataStructures;

public class ClientInput {
    private final String uuid;
    private final int keyInputCode;

    public ClientInput(String uuid, int keyInputCode) {
        this.uuid = uuid;
        this.keyInputCode = keyInputCode;
    }

    public int keyInput() {return this.keyInputCode; }
    public String uuid() {return this.uuid; }
}

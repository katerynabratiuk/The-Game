package org.lib.data_structures.payloads;

public class Actor extends Payload {
    private String clientId;
    private Coordinates coordinates;

    public Actor(String clientId,Coordinates coordinates) {
        this.clientId = clientId;
        this.coordinates = coordinates;
    }

    public Actor() {}

    public String getClientId() {
        return clientId;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates newCoords) {
        this.coordinates = newCoords;
    }
}

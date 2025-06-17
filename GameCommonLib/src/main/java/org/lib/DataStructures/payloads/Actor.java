package org.lib.DataStructures.payloads;

public class Actor extends Payload {
    private final String uuid;
    private Coordinates coordinates;
    public boolean someFutureFeatureEnabled;

    public Actor(String clientId,Coordinates coordinates) {
        this.uuid = clientId;
        this.coordinates = coordinates;
    }

    public String getClientId() {
        return uuid;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates newCoords) {
        this.coordinates = newCoords;
    }
}

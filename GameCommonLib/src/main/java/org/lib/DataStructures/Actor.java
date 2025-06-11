package org.lib.DataStructures;

import java.io.Serializable;

public class Actor implements Serializable {
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

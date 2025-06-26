package org.lib.data_structures.payloads.actors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data_structures.payloads.Payload;

import java.awt.*;

@Getter @Setter
@NoArgsConstructor
public class Actor extends Payload {
    private String clientUUID;

    private Coordinates coordinates;

    @JsonIgnore
    private boolean pendingDestroy;

    @JsonIgnore
    private boolean killed;

    private double radius = 10; // circular space occupied by actor
    private String imagePath;

    private int R = 0;
    private int G = 0;
    private int B = 0;


    public Actor(Coordinates coordinates, String clientUUID) {
        this.coordinates = coordinates;
        this.clientUUID = clientUUID;
    }

    public void updateColor(Color color) {
        setR(color.getRed());
        setG(color.getGreen());
        setB(color.getBlue());
    }

    public Color color() {
        return new Color(R, G, B);
    }

    public void OnCollision(Actor target) {}
    public void OnDeath() {}
}

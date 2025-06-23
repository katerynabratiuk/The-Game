package org.lib.data_structures.payloads.actors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.lib.data_structures.payloads.game.Coordinates;
import org.lib.data_structures.payloads.Payload;

import java.awt.*;


@NoArgsConstructor
public class Actor extends Payload {
// consider necessity
// @JsonIgnore @Getter
// private final String uuid = UUID.randomUUID().toString();

// add drawing strategy

    @JsonIgnore @Getter @Setter
    private String clientUUID;

    @Getter @Setter
    private Coordinates coordinates;

    @JsonIgnore @Getter @Setter
    private boolean pendingDestroy;

    @Getter @Setter
    private double radius = 10; // circular space occupied by actor

    @Getter @Setter
    private int R = 0;

    @Getter @Setter
    private int G = 0;

    @Getter @Setter
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
}

package org.lib.data_structures.payloads;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Coordinates extends Payload {
    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x  = x;
        this.y = y;
    }
}
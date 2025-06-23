package org.lib.data_structures.payloads.game;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lib.data_structures.payloads.Payload;

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
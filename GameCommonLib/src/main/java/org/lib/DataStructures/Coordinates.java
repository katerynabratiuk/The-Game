package org.lib.DataStructures;
import java.io.Serializable;

public class Coordinates implements Serializable {
    private final int x;
    private final int y;

    public Coordinates(int x, int y) {
        this.x  = x;
        this.y = y;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }
}
package org.server.db.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Pick {

    private Integer id;
    private GameCharacter gameCharacter;
    private Item weapon;
    private Item powerUp;

    public Pick(Integer id, Item weapon, Item powerUp) {
        this.id = id;
        this.weapon = weapon;
        this.powerUp = powerUp;
    }

    public Pick() {
    }

}

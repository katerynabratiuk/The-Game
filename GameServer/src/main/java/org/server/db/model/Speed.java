package org.server.db.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Speed extends Item {

    private Double power;
    private Integer time;

    public Speed() {
        super(ItemType.SPEED);
    }

    public Speed(Integer id, String name, String imagePath, Double power, Integer time) {
        super(ItemType.SPEED);
        this.setId(id);
        this.setName(name);
        this.setImagePath(imagePath);
        this.power = power;
        this.time = time;
    }
}

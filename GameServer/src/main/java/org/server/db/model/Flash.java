package org.server.db.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Flash extends Item{

    private Integer flashTime;
    private Double radius;

    public Flash(Integer id, String name, String imagePath, Integer flashTime, Double radius) {
        super(ItemType.FLASH);
        this.setId(id);
        this.setName(name);
        this.setImagePath(imagePath);
        this.flashTime = flashTime;
        this.radius = radius;
    }

    public Flash() {
        super(ItemType.FLASH);
    }

}

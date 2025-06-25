package org.server.db.model;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public abstract class Item {
    private final ItemType type;
    private Integer id;
    private String name;
    private String imagePath;

    public Item(ItemType itemType) {
        type = itemType;
    }

    public enum ItemType {
        WEAPON, HEAL, FLASH, SPEED
    }

}

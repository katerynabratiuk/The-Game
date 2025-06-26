package org.server.db.model;

import lombok.Getter;
import lombok.Setter;
import org.lib.data.dto.ItemDTO;


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

    public static ItemDTO toItemDto(Item item) {
        return new ItemDTO(item.getId(), item.getName(), item.imagePath);
    }

}

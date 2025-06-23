package org.server.db.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Pick {

    private Integer id;
    private Character character;
    private List<Item> items;

    public Pick(Integer id, List<Item> items) {
        this.id = id;
        this.items = items;
    }

    public Pick() {
    }

    public void addItem(Item item)
    {
        this.items.add(item);
    }

}

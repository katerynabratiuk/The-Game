package org.server.db.repository;

import org.server.db.model.Item;

import java.util.List;

public interface ItemRepository {

    Item get(Integer id);
    List<Item> getItemsByType(Item.ItemType type);
}

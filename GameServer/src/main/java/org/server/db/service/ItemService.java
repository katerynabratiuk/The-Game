package org.server.db.service;


import org.server.db.model.Item;
import org.server.db.repository.ItemRepository;

import java.util.List;

public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item getItem(Integer id) {
        return itemRepository.get(id);
    }

    public List<Item> getItemsByType(Item.ItemType type) {
        return itemRepository.getItemsByType(type);
    }
}

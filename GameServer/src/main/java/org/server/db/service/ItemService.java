package org.server.db.service;


import org.lib.data.dto.ItemDTO;
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

    public List<ItemDTO> getItemsByType(Item.ItemType type) {
        return itemRepository.getItemsByType(type).stream().map(this::mapToDto).toList();
    }

    private ItemDTO mapToDto(Item item)
    {
        return new ItemDTO(item.getId(), item.getName(), item.getImagePath());
    }

}

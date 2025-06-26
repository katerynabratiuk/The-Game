package org.server.db.repository;

import org.server.db.model.Item;
import org.server.db.repository.criteria.WeaponSearchCriteria;

import java.util.List;

public interface ItemRepository {

    Item get(Integer id);
    List<Item> getItemsByType(Item.ItemType type);
    List<Item> filter(WeaponSearchCriteria criteria);
}

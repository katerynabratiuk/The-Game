package org.server.db.repository.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.server.db.model.Item;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeaponSearchCriteria {
    private String name;
    private Item.ItemType type;
    private List<String> sortBy;

}


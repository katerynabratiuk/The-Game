package org.server.db.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lib.data.dto.ItemDTO;
import org.server.db.model.Item.ItemType;
import org.server.db.model.Weapon;
import org.server.db.repository.ItemRepository;
import org.server.db.repository.criteria.WeaponSearchCriteria;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    private ItemRepository itemRepository;
    private ItemService itemService;

    @BeforeEach
    void setup() {
        itemRepository = mock(ItemRepository.class);
        itemService = new ItemService(itemRepository);
    }

    @Test
    void testGetItem() {
        Weapon weapon = new Weapon(1, "Rocket", "/img/rocket.png", 50, 0.3, 1, 3);
        when(itemRepository.get(1)).thenReturn(weapon);

        var result = itemService.getItem(1);

        assertNotNull(result);
        assertEquals("Rocket", result.getName());
        verify(itemRepository).get(1);
    }

    @Test
    void testGetItemsByType() {
        List<Weapon> weapons = Arrays.asList(
                new Weapon(1, "Rocket", "/img/rocket.png", 50, 0.3, 1, 3),
                new Weapon(2, "Pistol", "/img/pistol.png", 20, 0.2, 2, 2)
        );
        when(itemRepository.getItemsByType(ItemType.WEAPON)).thenReturn(List.copyOf(weapons));

        List<ItemDTO> result = itemService.getItemsByType(ItemType.WEAPON);

        assertEquals(2, result.size());
        assertEquals("Rocket", result.get(0).getName());
        assertEquals("Pistol", result.get(1).getName());
        verify(itemRepository).getItemsByType(ItemType.WEAPON);
    }

    @Test
    void testFilter() {
        WeaponSearchCriteria criteria = new WeaponSearchCriteria();
        criteria.setName("rocket");
        criteria.setType(ItemType.WEAPON);

        List<Weapon> filtered = List.of(
                new Weapon(3, "Rocket Launcher", "/img/rocket.png", 90, 0.1, 2, 4)
        );
        when(itemRepository.filter(criteria)).thenReturn(List.copyOf(filtered));

        List<ItemDTO> result = itemService.filter(criteria);

        assertEquals(1, result.size());
        assertEquals("Rocket Launcher", result.get(0).getName());
        verify(itemRepository).filter(criteria);
    }
}

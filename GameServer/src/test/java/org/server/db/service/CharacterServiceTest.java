package org.server.db.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lib.data.dto.CharacterDTO;
import org.server.db.model.GameCharacter;
import org.server.db.repository.CharacterRepository;
import org.server.db.repository.criteria.CharacterSearchCriteria;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CharacterServiceTest {

    private CharacterRepository characterRepository;
    private CharacterService characterService;

    @BeforeEach
    void setup() {
        characterRepository = mock(CharacterRepository.class);
        characterService = new CharacterService(characterRepository);
    }

    @Test
    void testGetCharacter_found() {
        GameCharacter character = new GameCharacter(1, "Tank", "img.png", "Big tank", 0.8, 30);
        when(characterRepository.get(1)).thenReturn(character);

        GameCharacter result = characterService.getCharacter(1);

        assertNotNull(result);
        assertEquals("Tank", result.getName());
        verify(characterRepository).get(1);
    }

    @Test
    void testGetCharacter_notFound() {
        when(characterRepository.get(99)).thenReturn(null);

        GameCharacter result = characterService.getCharacter(99);

        assertNull(result);
        verify(characterRepository).get(99);
    }

    @Test
    void testGetAllCharacters() {
        List<GameCharacter> characters = Arrays.asList(
                new GameCharacter(1, "Fast", "fast.png", "Quick one", 1.5, 20),
                new GameCharacter(2, "Heavy", "heavy.png", "Slow tank", 0.5, 40)
        );
        when(characterRepository.getAll()).thenReturn(characters);

        List<CharacterDTO> result = characterService.getAllCharacters();

        assertEquals(2, result.size());
        assertEquals("Fast", result.get(0).getName());
        assertEquals("Heavy", result.get(1).getName());
        verify(characterRepository).getAll();
    }

    @Test
    void testFilterCharacters() {
        CharacterSearchCriteria criteria = new CharacterSearchCriteria();
        criteria.setName("fast");
        criteria.setFast(true);

        List<GameCharacter> filtered = List.of(
                new GameCharacter(3, "Fasty", "f.png", "Zoom zoom", 1.8, 15)
        );

        when(characterRepository.filter(criteria)).thenReturn(filtered);

        List<CharacterDTO> result = characterService.filter(criteria);

        assertEquals(1, result.size());
        assertEquals("Fasty", result.get(0).getName());
        verify(characterRepository).filter(criteria);
    }
}

package org.server.db.service;


import org.server.db.model.Character;
import org.server.db.repository.CharacterRepository;

import java.util.List;

public class CharacterService {

    private final CharacterRepository characterRepository;

    public CharacterService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    public Character getCharacter(Integer id) {
        return characterRepository.get(id);
    }

    public List<Character> getAllCharacters() {
        return characterRepository.getAll();
    }
}

package org.server.db.service;


import org.lib.data_structures.dto.CharacterDTO;
import org.server.db.model.GameCharacter;
import org.server.db.repository.CharacterRepository;
import org.server.db.repository.criteria.CharacterSearchCriteria;

import java.util.List;

public class CharacterService {

    private final CharacterRepository characterRepository;

    public CharacterService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    public GameCharacter getCharacter(Integer id) {
        return characterRepository.get(id);
    }

    public List<CharacterDTO> getAllCharacters() {
        return characterRepository.getAll().stream().map(this::mapToDTO).toList();
    }

    public List<CharacterDTO> filter(CharacterSearchCriteria criteria)
    {
        return characterRepository.filter(criteria).stream().map(this::mapToDTO).toList();
    }

    private CharacterDTO mapToDTO(GameCharacter gameCharacter)
    {
        return new CharacterDTO(
                gameCharacter.getId(),
                gameCharacter.getName(),
                gameCharacter.getDescription(),
                gameCharacter.getImagePath()
        );
    }
}

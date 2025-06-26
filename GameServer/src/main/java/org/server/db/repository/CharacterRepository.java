package org.server.db.repository;

import org.server.db.model.GameCharacter;
import org.server.db.repository.criteria.CharacterSearchCriteria;

import java.util.List;

public interface CharacterRepository {

    GameCharacter get(Integer id);
    List<GameCharacter> getAll();
    public List<GameCharacter> filter(CharacterSearchCriteria criteria);
}

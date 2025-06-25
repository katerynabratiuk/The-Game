package org.server.db.repository;

import org.server.db.model.GameCharacter;

import java.util.List;

public interface CharacterRepository {

    GameCharacter get(Integer id);
    List<GameCharacter> getAll();

}

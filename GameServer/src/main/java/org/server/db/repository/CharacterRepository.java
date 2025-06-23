package org.server.db.repository;

import org.server.db.model.Character;

import java.util.List;

public interface CharacterRepository {

    Character get(Integer id);
    List<Character> getAll();

}

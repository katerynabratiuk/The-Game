package org.server.db.service;

import org.server.db.model.Pick;
import org.server.db.repository.PickRepository;

import java.util.List;

public class PickService {

    private final PickRepository pickRepository;

    public PickService(PickRepository pickRepository) {
        this.pickRepository = pickRepository;
    }

    public Pick createPick(Pick pick) {
        return pickRepository.create(pick);
    }

    public Pick updatePick(Pick pick) {
        return pickRepository.update(pick);
    }

    public void deletePick(Integer pickId) {
        pickRepository.delete(pickId);
    }

    public Pick getPick(Integer pickId) {
        return pickRepository.get(pickId);
    }

    public List<Pick> getAllPicks() {
        return pickRepository.getAll();
    }
}

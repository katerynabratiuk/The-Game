package org.server.db.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Heal extends Item{

    private Integer power;
    private Integer time;

    public Heal(Integer id, String name, Integer power, Integer time) {
        this.setId(id);
        this.setName(name);
        this.power = power;
        this.time = time;
    }

    public Heal() {
    }
}

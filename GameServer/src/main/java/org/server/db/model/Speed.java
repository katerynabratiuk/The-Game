package org.server.db.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Speed extends Item {

    private Integer power;
    private Integer time;

    public Speed() {
    }

    public Speed(Integer id, String name, Integer power, Integer time) {
        this.setId(id);
        this.setName(name);
        this.power = power;
        this.time = time;
    }
}

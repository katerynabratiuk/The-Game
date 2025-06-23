package org.server.db.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Gun extends Item{

    private Integer damage;
    private Double spray;
    private Integer rof;

    public Gun(Integer id, String name,Integer damage, Double spray, Integer rof) {
        this.setId(id);
        this.setName(name);
        this.damage = damage;
        this.spray = spray;
        this.rof = rof;
    }

    public Gun() {
    }

}

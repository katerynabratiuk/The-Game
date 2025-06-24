package org.server.db.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Gun extends Item{

    private Integer damage;
    private Double spray;
    private Integer rof;
    private Integer lifespan;

    public Gun(Integer id, String name,Integer damage, Double spray, Integer rof, Integer lifespan) {
        super(ItemType.GUN);
        this.setId(id);
        this.setName(name);
        this.damage = damage;
        this.spray = spray;
        this.rof = rof;
        this.lifespan = lifespan;
    }

    public Gun() {
        super(ItemType.GUN);
    }

}

package org.server.db.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Weapon extends Item{

    private Integer damage;
    private Double spray;
    private Integer rof;
    private Integer lifespan;

    public Weapon(Integer id, String name, String imagePath, Integer damage, Double spray, Integer rof, Integer lifespan) {
        super(ItemType.WEAPON);
        this.setId(id);
        this.setName(name);
        this.setImagePath(imagePath);
        this.damage = damage;
        this.spray = spray;
        this.rof = rof;
        this.lifespan = lifespan;
    }

    public Weapon() {
        super(ItemType.WEAPON);
    }

}

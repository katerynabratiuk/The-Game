package org.server.db.factory;

import org.server.db.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemFactory {

    public static Item create(Item.ItemType type, int id, String name, String imagePath, ResultSet rs) throws SQLException {
        return switch (type) {
            case WEAPON -> new Weapon(id, name, imagePath,
                    rs.getInt("damage"),
                    rs.getDouble("spray"),
                    rs.getInt("rof"),
                    rs.getInt("lifespan"));
            case HEAL -> new Heal(id, name, imagePath,
                    rs.getInt("heal_points"),
                    rs.getInt("time"));
            case FLASH -> new Flash(id, name, imagePath,
                    rs.getInt("blindness_time"),
                    rs.getDouble("radius"));
            case SPEED -> new Speed(id, name, imagePath,
                    rs.getDouble("power"),
                    rs.getInt("time"));
        };
    }
}

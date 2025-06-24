package org.server.db.factory;

import org.server.db.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemFactory {

    public static Item create(Item.ItemType type, int id, String name, ResultSet rs) throws SQLException {
        return switch (type) {
            case GUN -> new Gun(id, name,
                    rs.getInt("damage"),
                    rs.getDouble("spray"),
                    rs.getInt("rof"),
                    rs.getInt("lifespan"));
            case HEAL -> new Heal(id, name,
                    rs.getInt("heal_points"),
                    rs.getInt("time"));
            case FLASH -> new Flash(id, name,
                    rs.getInt("blindness_time"),
                    rs.getDouble("radius"));
            case SPEED -> new Speed(id, name,
                    rs.getDouble("power"),
                    rs.getInt("time"));
        };
    }
}

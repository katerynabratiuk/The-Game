package org.server.db.repository.implementation;

import org.server.db.database_access.DbConnection;
import org.server.db.factory.ItemFactory;
import org.server.db.model.Item;
import org.server.db.repository.ItemRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemRepositoryImpl implements ItemRepository {

    @Override
    public Item get(Integer id) {
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM item WHERE id_item = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return null;

            String name = rs.getString("name");
            Item.ItemType type = Item.ItemType.valueOf(rs.getString("type").toUpperCase());

            String subtypeQuery = "SELECT * FROM " + type.name().toLowerCase() + " WHERE id_item = ?";
            try (PreparedStatement subStmt = conn.prepareStatement(subtypeQuery)) {
                subStmt.setInt(1, id);
                ResultSet subRs = subStmt.executeQuery();

                if (subRs.next()) {
                    return ItemFactory.create(type, id, name, subRs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<Item> getItemsByType(Item.ItemType type) {
        List<Item> result = new ArrayList<>();
        String table = type.name().toLowerCase();

        String sql = "SELECT i.id_item, i.name, i.type, t.* " +
                "FROM item i JOIN " + table + " t ON i.id_item = t.id_item " +
                "WHERE i.type = ?";

        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, type.name().toLowerCase());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Item item = ItemFactory.create(type, rs.getInt("id_item"), rs.getString("name"), rs);
                result.add(item);
            }

            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

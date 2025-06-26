package org.server.db.repository.implementation;

import org.server.db.database_access.DbConnection;
import org.server.db.model.GameCharacter;
import org.server.db.repository.CharacterRepository;
import org.server.db.repository.criteria.CharacterSearchCriteria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CharacterRepositoryImpl implements CharacterRepository {

    @Override
    public GameCharacter get(Integer id) {
        String sql = "SELECT * FROM character WHERE id_character = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractCharacterFromResultSet(rs);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameCharacter> getAll() {
        List<GameCharacter> result = new ArrayList<>();
        String sql = "SELECT * FROM character";

        try (Connection connection = DbConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(extractCharacterFromResultSet(rs));
            }

            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameCharacter> filter(CharacterSearchCriteria criteria) {
        List<GameCharacter> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM character WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (criteria.getName() != null && !criteria.getName().isEmpty()) {
            sql.append(" AND LOWER(name) LIKE ?");
            params.add("%" + criteria.getName().toLowerCase() + "%");
        }

        if (Boolean.TRUE.equals(criteria.getFast())) {
            sql.append(" AND speed > ?");
            params.add(1);
        }

        if (Boolean.TRUE.equals(criteria.getArmor())) {
            sql.append(" AND heart_points >= ?");
            params.add(10);
        }

        System.out.println(sql);

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(extractCharacterFromResultSet(rs));
            }

            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private GameCharacter extractCharacterFromResultSet(ResultSet rs) throws SQLException {
        return new GameCharacter(
                rs.getInt("id_character"),
                rs.getString("name"),
                rs.getString("image_path"),
                rs.getString("description"),
                rs.getDouble("speed"),
                rs.getInt("heart_points")
        );
    }
}

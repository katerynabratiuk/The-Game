package org.server.db.repository.implementation;

import org.server.db.database_access.DbConnection;
import org.server.db.model.GameCharacter;
import org.server.db.repository.CharacterRepository;

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

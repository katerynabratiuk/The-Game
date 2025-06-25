package org.server.db.repository.implementation;

import org.server.db.database_access.DbConnection;
import org.server.db.model.GameCharacter;
import org.server.db.model.Pick;
import org.server.db.repository.PickRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PickRepositoryImpl implements PickRepository {

    @Override
    public Pick create(Pick pick) {
        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO pick(id_pick, id_character) VALUES (?, ?)"
            );
            stmt.setInt(1, pick.getId());
            stmt.setInt(2, pick.getGameCharacter().getId());
            stmt.executeUpdate();
            return pick;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer pickId) {
        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM pick WHERE id_pick = ?"
            );
            stmt.setInt(1, pickId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Pick update(Pick pick) {
        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE pick SET id_character = ? WHERE id_pick = ?"
            );
            stmt.setInt(1, pick.getGameCharacter().getId());
            stmt.setInt(2, pick.getId());
            stmt.executeUpdate();
            return pick;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Pick get(Integer pickId) {
        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM pick WHERE id_pick = ?"
            );
            stmt.setInt(1, pickId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractPickFromResultSet(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Pick> getAll() {
        try (Connection connection = DbConnection.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM pick");

            List<Pick> result = new ArrayList<>();
            while (rs.next()) {
                result.add(extractPickFromResultSet(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Pick extractPickFromResultSet(ResultSet rs) throws SQLException {
        Pick pick = new Pick();
        pick.setId(rs.getInt("id_pick"));

        GameCharacter gameCharacter = new GameCharacter();
        gameCharacter.setId(rs.getInt("id_character"));

        pick.setGameCharacter(gameCharacter);
        return pick;
    }
}

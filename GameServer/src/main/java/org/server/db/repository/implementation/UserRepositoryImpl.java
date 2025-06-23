package org.server.db.repository.implementation;

import org.server.db.database_access.DbConnection;
import org.server.db.model.User;
import org.server.db.repository.UserRepository;
import org.server.db.repository.criteria.UserSearchCriteria;
import org.server.db.util.PasswordHasher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {


    @Override
    public User create(User user) {
        try(Connection connection = DbConnection.getConnection()) {

            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO game_user(username, password, country) VALUES(?,?,?)");
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, PasswordHasher.hash(user.getPassword()));
            pstmt.setString(3, user.getCountry());
            pstmt.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String username) {
        try(Connection connection = DbConnection.getConnection()) {

            PreparedStatement pstmt = connection.prepareStatement("DELETE FROM game_user WHERE username = ?");
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User get(String username) {
        try(Connection connection = DbConnection.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM game_user WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        try(Connection connection = DbConnection.getConnection()) {

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM game_user");

            List<User> result = new ArrayList<>();

            while(rs.next())
            {
                result.add(extractUserFromResultSet(rs));
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> filter(UserSearchCriteria criteria) {
        List<User> users = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM game_user WHERE 1=1");

        if (criteria.getName() != null && !criteria.getName().isBlank()) {
            sql.append(" AND username ILIKE ?");
            params.add("%" + criteria.getName() + "%");
        }

        if (criteria.getCountry() != null && !criteria.getCountry().isBlank()) {
            sql.append(" AND country = ?");
            params.add(criteria.getCountry());
        }

        if (criteria.getKills() != null) {
            sql.append(" AND (SELECT SUM(kill_count) FROM session s WHERE s.username = game_user.username) >= ?");
            params.add(criteria.getKills());
        }

        if (criteria.getPage() != null && criteria.getPageSize() != null) {
            sql.append(" LIMIT ? OFFSET ?");
            params.add(criteria.getPageSize());
            params.add(criteria.getPage() * criteria.getPageSize());
        }

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }

            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(rs.getString("username"), rs.getString("password"), rs.getString("country"));
    }

    public boolean existsByUsername(String username) {
        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT 1 FROM game_user WHERE username = ? LIMIT 1"
            );
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

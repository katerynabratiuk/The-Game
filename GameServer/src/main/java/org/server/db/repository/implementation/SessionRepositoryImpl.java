package org.server.db.repository.implementation;

import org.server.db.database_access.DbConnection;
import org.server.db.model.Pick;
import org.server.db.model.Session;
import org.server.db.model.User;
import org.server.db.repository.SessionRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SessionRepositoryImpl implements SessionRepository {
    @Override
    public Session create(Session session) {
        try(Connection connection = DbConnection.getConnection()) {

            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO session(start_time, username, id_pick) VALUES(?,?,?)");
            pstmt.setObject(1, session.getStartTime());
            pstmt.setString(2, session.getUser().getUsername());
            pstmt.setInt(3, session.getPick().getId());

            pstmt.executeUpdate();
            return session;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        try(Connection connection = DbConnection.getConnection()) {

            PreparedStatement pstmt = connection.prepareStatement("DELETE FROM session WHERE id_session = ?");
            pstmt.setObject(1, id);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Session update(Session session) {
        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE session SET start_time = ?, end_time = ?, kill_count = ?, username = ?, id_pick = ? WHERE id_session = ?"
            );
            pstmt.setObject(1, session.getStartTime());
            pstmt.setObject(2, session.getEndTime());
            pstmt.setInt(3, session.getKillCount());
            pstmt.setString(4, session.getUser().getUsername());
            pstmt.setInt(5, session.getPick().getId());
            pstmt.setInt(6, session.getId());

            pstmt.executeUpdate();
            return session;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Session get(Integer id) {
        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM session WHERE id_session = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractSessionFromResultSet(rs);
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Session> getAll() {
        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM session");
            ResultSet rs = stmt.executeQuery();

            List<Session> result = new ArrayList<>();
            while (rs.next()) {
                result.add(extractSessionFromResultSet(rs));
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addEndTime(Session session) {
        try(Connection connection = DbConnection.getConnection()) {

            PreparedStatement pstmt = connection.prepareStatement("UPDATE session SET end_time = ? WHERE id_session = ?");
            pstmt.setObject(1, session.getEndTime());
            pstmt.setInt(2, session.getId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addKillCount(Session session) {
        try(Connection connection = DbConnection.getConnection()) {

            PreparedStatement pstmt = connection.prepareStatement("UPDATE session SET kill_count = ? WHERE id_session = ?");
            pstmt.setObject(1, session.getKillCount());
            pstmt.setInt(2, session.getId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Session> getUsersSessions(User user) {
        try(Connection connection = DbConnection.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM session WHERE username = ?");
            stmt.setString(1, user.getUsername());
            ResultSet rs = stmt.executeQuery();

            List<Session> result = new ArrayList<>();

            while(rs.next())
            {
                 result.add(extractSessionFromResultSet(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Session extractSessionFromResultSet(ResultSet rs) throws SQLException {
        Session session = new Session();
        session.setId(rs.getInt("id_session"));
        session.setStartTime(rs.getObject("start_time", LocalDateTime.class));
        session.setEndTime(rs.getObject("end_time", LocalDateTime.class));
        session.setKillCount(rs.getInt("kill_count"));

        User user = new User();
        user.setUsername(rs.getString("username"));
        session.setUser(user);

        Pick pick = new Pick();
        pick.setId(rs.getInt("id_pick"));
        session.setPick(pick);

        return session;
    }


}

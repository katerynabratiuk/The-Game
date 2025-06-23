package org.server.db.repository;

import org.server.db.database_access.DbConnection;
import org.server.db.model.User;
import org.server.db.repository.criteria.UserSearchCriteria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface UserRepository extends GenericRepository<User, String> {
    List<User> filter(UserSearchCriteria criteria);
    boolean existsByUsername(String username);
}

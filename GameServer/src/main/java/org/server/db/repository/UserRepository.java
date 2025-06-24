package org.server.db.repository;

import org.server.db.model.User;
import org.server.db.repository.criteria.UserSearchCriteria;

import java.util.List;

public interface UserRepository extends GenericRepository<User, String> {
    List<User> filter(UserSearchCriteria criteria);
    boolean existsByUsername(String username);
    boolean isValidForRegistration(User user);
}

package org.server.db.mapper;

import org.server.db.dto.UserDTO;
import org.server.db.model.User;

public class UserMapper {
    public static UserDTO toDto(User user) {
        return new UserDTO(user.getUsername(), user.getCountry());
    }
}
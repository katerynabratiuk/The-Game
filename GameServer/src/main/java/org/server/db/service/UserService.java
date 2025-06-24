package org.server.db.service;

import org.server.db.dto.UserDTO;
import org.server.db.mapper.UserMapper;
import org.server.db.model.User;
import org.server.db.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isValidUser(User user) {
        return user.getUsername() != null && user.getUsername().length() > 3 &&
                user.getPassword() != null && user.getPassword().length() > 5 &&
                !userRepository.existsByUsername(user.getUsername());
    }

    public User registerUser(User user) {
        if (!isValidUser(user)) {
            throw new IllegalArgumentException("Invalid user data or username already exists");
        }
        return userRepository.create(user);
    }

    public UserDTO getUser(String username) {
        User user = userRepository.get(username);
        if (user == null) return null;
        return UserMapper.toDto(user);
    }


    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean correctCredentials(User user) {
        return userRepository.isValidForRegistration(user);
    }

}

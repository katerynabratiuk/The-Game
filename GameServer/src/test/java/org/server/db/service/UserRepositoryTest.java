package org.server.db.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.server.db.dto.UserDTO;
import org.server.db.model.User;
import org.server.db.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUser_found() {
        User user = new User("userrr", "pass");
        when(userRepo.get("userrr")).thenReturn(user);

        UserDTO result = userService.getUser("userrr");

        assertNotNull(result);
        assertEquals("userrr", result.getUsername());
        verify(userRepo).get("userrr");
    }

    @Test
    void testGetUser_notFound() {
        when(userRepo.get("ghost")).thenReturn(null);

        UserDTO result = userService.getUser("ghost");

        assertNull(result);
        verify(userRepo).get("ghost");
    }

    @Test
    void testUserExists_true() {
        when(userRepo.existsByUsername("kate")).thenReturn(true);

        assertTrue(userService.userExists("kate"));
        verify(userRepo).existsByUsername("kate");
    }

    @Test
    void testUserExists_false() {
        when(userRepo.existsByUsername("ghost")).thenReturn(false);

        assertFalse(userService.userExists("ghost"));
        verify(userRepo).existsByUsername("ghost");
    }

    @Test
    void testCorrectCredentials_true() {
        User user = new User("john", "secure");
        when(userRepo.isValidForRegistration(user)).thenReturn(true);

        assertTrue(userService.correctCredentials(user));
        verify(userRepo).isValidForRegistration(user);
    }

    @Test
    void testCorrectCredentials_false() {
        User user = new User("john", "wrong");
        when(userRepo.isValidForRegistration(user)).thenReturn(false);

        assertFalse(userService.correctCredentials(user));
        verify(userRepo).isValidForRegistration(user);
    }

    @Test
    void testIsValidUser_validAndUnique() {
        User user = new User("validuser", "123456");
        when(userRepo.existsByUsername("validuser")).thenReturn(false);

        assertTrue(userService.isValidUser(user));
        verify(userRepo).existsByUsername("validuser");
    }

    @Test
    void testIsValidUser_invalidShortUsername() {
        User user = new User("a", "123456");
        assertFalse(userService.isValidUser(user));
        verify(userRepo, never()).existsByUsername(any());
    }

    @Test
    void testIsValidUser_usernameAlreadyExists() {
        User user = new User("existing", "123456");
        when(userRepo.existsByUsername("existing")).thenReturn(true);

        assertFalse(userService.isValidUser(user));
    }

    @Test
    void testRegisterUser_success() {
        User user = new User("newuser", "securepass");
        when(userRepo.existsByUsername("newuser")).thenReturn(false);
        when(userRepo.create(user)).thenReturn(user);

        User result = userService.registerUser(user);

        assertEquals(user, result);
        verify(userRepo).create(user);
    }

    @Test
    void testRegisterUser_failsOnValidation() {
        User user = new User("aa", "bb"); // too short

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(user)
        );

        assertEquals("Invalid user data or username already exists", ex.getMessage());
        verify(userRepo, never()).create(any());
    }
}

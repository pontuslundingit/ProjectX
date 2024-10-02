package com.example.projectx.services;

import com.example.projectx.models.User;
import com.example.projectx.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void serUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUserIfNotExists_UserAlreadyExists() {
        String username = "existingUser";
        User existingUser = new User();
        existingUser.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));

        User result = userService.registerUserIfNotExists(username);

        assertEquals(existingUser, result);
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterUserIfNotExists_NewUser() {
        String username = "newUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        User newUser = new User();
        newUser.setUsername(username);

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User result = userService.registerUserIfNotExists(username);

        assertEquals(newUser, result);
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindByUsername_UserExists() {
        String username = "existingUser";
        User existingUser = new User();
        existingUser.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));

        Optional<User> result = userService.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(existingUser, result.get());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testFindByUsername_UserDoesNotExist() {
        String username = "nonExistingUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<User> result = userService.findByUsername(username);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsername(username);
    }

}

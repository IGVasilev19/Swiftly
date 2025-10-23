package com.swiftly.application.user;

import com.swiftly.application.user.port.outbound.UserPort;
import com.swiftly.domain.User;
import com.swiftly.domain.enums.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserServiceTest {

    @Mock
    private UserPort userPort;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getByEmail_returnsUser() {
        // Arrange
        String email = "test@example.com";
        User user = new User(email, "hashedPassword", Role.RENTER, false);

        when(userPort.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getByEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.get().getEmail());
        verify(userPort).findByEmail(email);
    }

    @Test
    void getByEmail_userNotFound_throwsException() {
        // Arrange
        String email = "missing@example.com";
        when(userPort.findByEmail(email)).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.getByEmail(email));

        assertEquals("User not found", ex.getMessage());
        verify(userPort).findByEmail(email);
    }

    @Test
    void existsByEmail_returnsTrue() {
        // Arrange
        String email = "exists@example.com";
        when(userPort.existsByEmail(email)).thenReturn(true);

        // Act
        boolean result = userService.existsByEmail(email);

        // Assert
        assertTrue(result);
        verify(userPort).existsByEmail(email);
    }

    @Test
    void existsByEmail_returnsFalse() {
        // Arrange
        String email = "notfound@example.com";
        when(userPort.existsByEmail(email)).thenReturn(false);

        // Act
        boolean result = userService.existsByEmail(email);

        // Assert
        assertFalse(result);
        verify(userPort).existsByEmail(email);
    }
}

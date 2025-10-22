package com.swiftly.application.user;

import com.swiftly.application.user.port.inbound.UserUseCase;
import com.swiftly.application.user.port.outbound.UserPort;
import com.swiftly.domain.User;
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
    private UserUseCase userUseCase;

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
        User user = new User();
        user.setEmail(email);
        when(userUseCase.getByEmail(email)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getByEmail(email);

        // Assert
        assertTrue(result.equals(user));
        assertEquals(email, result.get().getEmail());
        verify(userUseCase, times(1)).getByEmail(email);
    }

    @Test
    void getByEmail_userNotFound() {
        //Arrange
        String email = "missing@example.com";
        when(userUseCase.getByEmail(email)).thenReturn(Optional.empty());

        //Act
        Optional<User> result = userService.getByEmail(email);

        //Assert
        assertTrue(result.equals(null));
        verify(userUseCase).getByEmail(email);
    }

    @Test
    void existsByEmail_returnsTrue() {
        //Arrange
        String email = "exists@example.com";
        when(userUseCase.existsByEmail(email)).thenReturn(true);

        //Act
        boolean result = userService.existsByEmail(email);

        //Assert
        assertTrue(result);
        verify(userUseCase).existsByEmail(email);
    }

    @Test
    void existsByEmail_returnsFalse() {
        //Arrange
        String email = "notfound@example.com";
        when(userUseCase.existsByEmail(email)).thenReturn(false);

        //Act
        boolean result = userService.existsByEmail(email);

        //Assert
        assertFalse(result);
    }
}
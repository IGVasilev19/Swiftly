package com.swiftly.application.user;

import com.swiftly.application.user.port.outbound.UserRepository;
import com.swiftly.domain.User;
import com.swiftly.domain.enums.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("unit")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getByEmail_returnsUser() {
        List<Role> roles = List.of(Role.RENTER);

        String email = "test@example.com";
        User user = new User( email, "hashedPassword", roles);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User result = userServiceImpl.getByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void getByEmail_userNotFound_throwsException() {
        String email = "missing@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userServiceImpl.getByEmail(email));

        assertEquals("User not found", ex.getMessage());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void existsByEmail_returnsTrue() {
        String email = "exists@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean result = userServiceImpl.existsByEmail(email);

        assertTrue(result);
        verify(userRepository).existsByEmail(email);
    }

    @Test
    void existsByEmail_returnsFalse() {
        String email = "notfound@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        boolean result = userServiceImpl.existsByEmail(email);

        assertFalse(result);
        verify(userRepository).existsByEmail(email);
    }
}

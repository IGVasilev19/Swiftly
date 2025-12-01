package com.swiftly.application.auth;


import com.swiftly.application.auth.port.inbound.JwtService;
import com.swiftly.application.auth.port.inbound.LogInService;
import com.swiftly.application.auth.port.inbound.RefreshTokenService;
import com.swiftly.application.helpers.PasswordHasher;
import com.swiftly.application.user.port.inbound.UserService;
import com.swiftly.domain.RefreshToken;
import com.swiftly.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit")
public class LogInServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private LogInServiceImpl logInService;


    private User requestedUser;
    private User storedUser;
    private RefreshToken refreshToken;

    @BeforeEach
    void setup() {
        requestedUser = new User();
        requestedUser.setEmail("test@mail.com");
        requestedUser.setPasswordHash("rawPass");

        storedUser = new User();
        storedUser.setEmail("test@mail.com");
        storedUser.setPasswordHash("hashedPass");

        refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token-123");
        refreshToken.setUser(storedUser);
    }

    @Test
    public void login_successfulAuthentication_returnsTokens() {
        try (MockedStatic<PasswordHasher> staticMock = Mockito.mockStatic(PasswordHasher.class)) {
            when(userService.getByEmail("test@mail.com")).thenReturn(storedUser);
            staticMock.when(() -> PasswordHasher.checkPassword("rawPass", "hashedPass")).thenReturn(true);
            when(refreshTokenService.createRefreshToken("test@mail.com")).thenReturn(refreshToken);
            when(jwtService.generateAccessToken(storedUser)).thenReturn("access-xyz");

            User result = logInService.login(requestedUser);

            assertNotNull(result);
            assertEquals("access-xyz", result.getAccessToken());
            assertEquals("refresh-token-123", result.getRefreshToken());
        }
    }

    @Test
    public void login_wrongPassword_throwsException() {
        try (MockedStatic<PasswordHasher> staticMock = Mockito.mockStatic(PasswordHasher.class)) {
            when(userService.getByEmail("test@mail.com")).thenReturn(storedUser);
            staticMock.when(() -> PasswordHasher.checkPassword("rawPass", "hashedPass")).thenReturn(false);

            assertThrows(IllegalArgumentException.class, () -> logInService.login(requestedUser));
        }
    }

    @Test
    public void refreshToken_validToken_returnsUpdatedAccessToken() {
        when(refreshTokenService.getByToken("rt-123")).thenReturn(refreshToken);
        when(refreshTokenService.verifyExpiration(refreshToken)).thenReturn(refreshToken);
        when(userService.getByEmail("test@mail.com")).thenReturn(storedUser);
        when(jwtService.generateAccessToken(storedUser)).thenReturn("new-access-555");

        User result = logInService.refreshToken("rt-123");

        assertNotNull(result);
        assertEquals("new-access-555", result.getAccessToken());
        assertEquals("refresh-token-123", result.getRefreshToken());
        verify(refreshTokenService).verifyExpiration(refreshToken);
    }

    @Test
    public void refreshToken_tokenNotFound_returnsNull() {
        when(refreshTokenService.getByToken("missing")).thenReturn(null);

        User result = logInService.refreshToken("missing");

        assertNull(result);
        assertNull(result);
        verify(refreshTokenService, never()).verifyExpiration(any());
    }

    @Test
    public void refreshToken_expiredToken_returnsNull() {
        when(refreshTokenService.getByToken("expired")).thenReturn(refreshToken);
        when(refreshTokenService.verifyExpiration(refreshToken)).thenReturn(null);

        User result = logInService.refreshToken("expired");

        assertNull(result);
    }

    @Test
    public void logout_validUserId_deletesRefreshToken() {
        logInService.logout(10);

        verify(refreshTokenService).deleteTokenById(10);
    }

    @Test
    public void logout_nullUserId_noOperation() {
        logInService.logout(null);

        verify(refreshTokenService, never()).deleteTokenById(any());
    }
}

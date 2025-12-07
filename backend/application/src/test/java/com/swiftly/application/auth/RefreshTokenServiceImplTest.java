package com.swiftly.application.auth;

import com.swiftly.application.auth.port.outbound.RefreshTokenRepository;
import com.swiftly.application.user.port.inbound.UserService;
import com.swiftly.domain.RefreshToken;
import com.swiftly.domain.User;
import com.swiftly.domain.enums.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit")
class RefreshTokenServiceImplTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    private User testUser;
    private RefreshToken existingToken;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(refreshTokenService, "refreshExpiration", 604800000L);
        
        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("test@example.com");
        testUser.setRoles(List.of(Role.OWNER));

        existingToken = new RefreshToken();
        existingToken.setId(1L);
        existingToken.setUser(testUser);
        existingToken.setToken("existing-token-123");
        existingToken.setExpiryDate(Instant.now().plusSeconds(3600));
        existingToken.setRevoked(false);
    }

    @Test
    void createRefreshToken_NoExistingToken_ShouldCreateNewToken() {
        when(userService.getByEmail("test@example.com")).thenReturn(testUser);
        when(refreshTokenRepository.findByUserId(1)).thenReturn(Optional.empty());
        
        RefreshToken savedToken = new RefreshToken();
        savedToken.setId(1L);
        savedToken.setUser(testUser);
        savedToken.setToken("new-token-456");
        savedToken.setExpiryDate(Instant.now().plusMillis(604800000));
        savedToken.setRevoked(false);
        
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(savedToken);

        RefreshToken result = refreshTokenService.createRefreshToken("test@example.com");

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertNotNull(result.getExpiryDate());
        assertFalse(result.isRevoked());
        assertEquals(testUser, result.getUser());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void createRefreshToken_ExistingValidToken_ShouldReturnExistingToken() {
        RefreshToken validExistingToken = new RefreshToken();
        validExistingToken.setId(1L);
        validExistingToken.setUser(testUser);
        validExistingToken.setToken("existing-token-123");
        validExistingToken.setExpiryDate(Instant.now().plusSeconds(3600));
        validExistingToken.setRevoked(false);

        when(userService.getByEmail("test@example.com")).thenReturn(testUser);
        when(refreshTokenRepository.findByUserId(1)).thenReturn(Optional.of(validExistingToken));

        RefreshToken result = refreshTokenService.createRefreshToken("test@example.com");

        assertNotNull(result);
        assertEquals("existing-token-123", result.getToken());
        verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
    }

    @Test
    void createRefreshToken_ExistingExpiredToken_ShouldCreateNewToken() {
        RefreshToken expiredToken = new RefreshToken();
        expiredToken.setId(1L);
        expiredToken.setUser(testUser);
        expiredToken.setToken("expired-token");
        expiredToken.setExpiryDate(Instant.now().minusSeconds(3600));
        expiredToken.setRevoked(false);

        when(userService.getByEmail("test@example.com")).thenReturn(testUser);
        when(refreshTokenRepository.findByUserId(1)).thenReturn(Optional.of(expiredToken));
        
        RefreshToken newToken = new RefreshToken();
        newToken.setId(2L);
        newToken.setUser(testUser);
        newToken.setToken("new-token-789");
        newToken.setExpiryDate(Instant.now().plusMillis(604800000));
        newToken.setRevoked(false);
        
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(newToken);

        RefreshToken result = refreshTokenService.createRefreshToken("test@example.com");

        assertNotNull(result);
        assertEquals("new-token-789", result.getToken());
        verify(refreshTokenRepository).delete(expiredToken);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void verifyExpiration_ValidToken_ShouldReturnToken() {
        RefreshToken validToken = new RefreshToken();
        validToken.setExpiryDate(Instant.now().plusSeconds(3600));
        validToken.setRevoked(false);

        RefreshToken result = refreshTokenService.verifyExpiration(validToken);

        assertNotNull(result);
        assertEquals(validToken, result);
        verify(refreshTokenRepository, never()).delete(any(RefreshToken.class));
    }

    @Test
    void verifyExpiration_ExpiredToken_ShouldDeleteAndReturnNull() {
        RefreshToken expiredToken = new RefreshToken();
        expiredToken.setExpiryDate(Instant.now().minusSeconds(3600));
        expiredToken.setRevoked(false);

        RefreshToken result = refreshTokenService.verifyExpiration(expiredToken);

        assertNull(result);
        verify(refreshTokenRepository).delete(expiredToken);
    }

    @Test
    void verifyExpiration_RevokedToken_ShouldDeleteAndReturnNull() {
        RefreshToken revokedToken = new RefreshToken();
        revokedToken.setExpiryDate(Instant.now().plusSeconds(3600));
        revokedToken.setRevoked(true);

        RefreshToken result = refreshTokenService.verifyExpiration(revokedToken);

        assertNull(result);
        verify(refreshTokenRepository).delete(revokedToken);
    }

    @Test
    void deleteTokenById_ShouldCallRepository() {
        refreshTokenService.deleteTokenById(1);

        verify(refreshTokenRepository).deleteById(1);
    }

    @Test
    void getByToken_ShouldReturnToken() {
        when(refreshTokenRepository.findByToken("test-token")).thenReturn(existingToken);

        RefreshToken result = refreshTokenService.getByToken("test-token");

        assertNotNull(result);
        assertEquals(existingToken, result);
        verify(refreshTokenRepository).findByToken("test-token");
    }

    @Test
    void getByToken_TokenNotFound_ShouldReturnNull() {
        when(refreshTokenRepository.findByToken("missing-token")).thenReturn(null);

        RefreshToken result = refreshTokenService.getByToken("missing-token");

        assertNull(result);
    }

    @Test
    void getByUserId_ShouldReturnOptional() {
        when(refreshTokenRepository.findByUserId(1)).thenReturn(Optional.of(existingToken));

        Optional<RefreshToken> result = refreshTokenService.getByUserId(1);

        assertTrue(result.isPresent());
        assertEquals(existingToken, result.get());
    }

    @Test
    void getByUserId_NoToken_ShouldReturnEmpty() {
        when(refreshTokenRepository.findByUserId(1)).thenReturn(Optional.empty());

        Optional<RefreshToken> result = refreshTokenService.getByUserId(1);

        assertTrue(result.isEmpty());
    }
}


package com.swiftly.application.auth;

import com.swiftly.domain.User;
import com.swiftly.domain.enums.user.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit")
class JwtServiceImplTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    private String testSecret;
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        testSecret = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        
        ReflectionTestUtils.setField(jwtService, "secret", testSecret);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 900000L);
    }

    @Test
    void generateAccessToken_WithUser_ShouldGenerateValidToken() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setRoles(List.of(Role.OWNER));

        String token = jwtService.generateAccessToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Base64.getDecoder().decode(testSecret)))
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        assertEquals("1", claims.getSubject());
        assertNotNull(claims.get("roles"));
    }

    @Test
    void generateAccessToken_WithUserDetails_ShouldUseUsernameAsSubject() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setRoles(List.of(Role.RENTER));

        String token = jwtService.generateAccessToken(user);

        assertNotNull(token);
        String username = jwtService.extractUsername(token);
        assertEquals("test@example.com", username);
    }

    @Test
    void extractUsername_ValidToken_ShouldReturnSubject() {
        User user = new User();
        user.setId(42);
        user.setEmail("test@example.com");
        user.setRoles(List.of(Role.OWNER));
        
        String token = jwtService.generateAccessToken(user);

        String username = jwtService.extractUsername(token);

        assertEquals("42", username);
    }

    @Test
    void extractUserId_ValidToken_ShouldReturnUserId() {
        User user = new User();
        user.setId(123);
        user.setEmail("test@example.com");
        user.setRoles(List.of(Role.OWNER));
        
        String token = jwtService.generateAccessToken(user);

        Integer userId = jwtService.extractUserId(token);

        assertEquals(123, userId);
    }

    @Test
    void extractUserId_InvalidSubject_ShouldReturnNull() {
        String token = Jwts.builder()
                .setSubject("invalid")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 900000))
                .signWith(secretKey)
                .compact();

        Integer userId = jwtService.extractUserId(token);

        assertNull(userId);
    }

    @Test
    void extractRoles_ValidToken_ShouldReturnRoles() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setRoles(List.of(Role.OWNER, Role.RENTER));
        
        String token = jwtService.generateAccessToken(user);

        List<String> roles = jwtService.extractRoles(token);

        assertNotNull(roles);
        assertTrue(roles.contains("ROLE_OWNER"));
        assertTrue(roles.contains("ROLE_RENTER"));
    }

    @Test
    void isValid_ValidTokenAndMatchingUserId_ShouldReturnTrue() {
        User user = new User();
        user.setId(99);
        user.setEmail("test@example.com");
        user.setRoles(List.of(Role.OWNER));
        
        String token = jwtService.generateAccessToken(user);

        boolean isValid = jwtService.isValid(token, 99);

        assertTrue(isValid);
    }

    @Test
    void isValid_ValidTokenButWrongUserId_ShouldReturnFalse() {
        User user = new User();
        user.setId(99);
        user.setEmail("test@example.com");
        user.setRoles(List.of(Role.OWNER));
        
        String token = jwtService.generateAccessToken(user);

        boolean isValid = jwtService.isValid(token, 100);

        assertFalse(isValid);
    }

    @Test
    void isValid_ExpiredToken_ShouldThrowException() {
        String expiredToken = Jwts.builder()
                .setSubject("1")
                .claim("roles", List.of("ROLE_OWNER"))
                .setIssuedAt(new Date(System.currentTimeMillis() - 2000000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000000))
                .signWith(secretKey)
                .compact();

        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, 
                () -> jwtService.isValid(expiredToken, 1));
    }
}


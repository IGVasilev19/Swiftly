package com.swiftly.application.auth;

import com.swiftly.application.auth.port.inbound.JwtUseCase;
import com.swiftly.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor

public class JwtService implements JwtUseCase {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateAccessToken(UserDetails userDetails) {
        String subject;

        if (userDetails instanceof User u && u.getId() != null) {
            subject = u.getId().toString();
        } else {
            subject = userDetails.getUsername();
        }

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Integer extractUserId(String token) {
        String sub = extractUsername(token);
        try {
            return Integer.valueOf(sub);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean isValid(String token, Integer userId) {
        final Integer subject = extractUserId(token);
        boolean subjectMatches = subject.equals(userId);
        return subjectMatches && !isExpired(token);
    }

    private boolean isExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}

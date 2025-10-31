package com.swiftly.application.auth;

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

public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateAccessToken(UserDetails userDetails) {
        // Prefer using user id as subject when available, fallback to username for compatibility
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

    public boolean isValid(String token, UserDetails userDetails) {
        // Backward compatible validation; retained for any legacy flow
        final String subject = extractUsername(token);
        boolean subjectMatches = subject.equals(userDetails.getUsername())
                || (userDetails instanceof User u && u.getId() != null && subject.equals(u.getId().toString()));
        return subjectMatches && !isExpired(token);
    }

    public boolean isValid(String token) {
        return !isExpired(token);
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

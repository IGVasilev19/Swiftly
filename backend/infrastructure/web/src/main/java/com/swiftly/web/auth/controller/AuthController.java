package com.swiftly.web.auth.controller;

import com.swiftly.application.auth.port.inbound.JwtService;
import com.swiftly.application.auth.port.inbound.LogInService;
import com.swiftly.application.auth.port.inbound.RegisterService;
import com.swiftly.web.auth.dto.LogInRequest;
import com.swiftly.web.auth.dto.LogInResponse;
import com.swiftly.web.auth.dto.RegisterRequest;
import com.swiftly.web.auth.mapper.LogInMapper;
import com.swiftly.web.auth.mapper.RegisterMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name="Auth")
@RequiredArgsConstructor
public class AuthController {
    private final RegisterService registerService;
    private final LogInService logInService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest)
    {
        try
        {
        registerService.register(RegisterMapper.toCommand(registerRequest));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "success", true,
                        "message", "Account created successfully!"
                ));

        } catch(IllegalArgumentException e)
        {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LogInRequest logInRequest)
    {
        try {
            LogInResponse response = LogInMapper.toLogInResponse(logInService.login(LogInMapper.toUser(logInRequest)));

            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", response.refreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/api/v1/auth/refresh")
                    .maxAge(7 * 24 * 60 * 60)
                    .sameSite("Strict")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(response.accessToken());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing cookies");
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refresh_token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing refresh token");
        }

        LogInResponse response = LogInMapper.toLogInResponse(logInService.refreshToken(refreshToken));
        return ResponseEntity.ok(Map.of("accessToken", response.accessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout( @RequestHeader("Authorization") String authHeader, HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/v1/auth/refresh")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        String accessToken = authHeader.replace("Bearer ", "");

        logInService.logout(jwtService.extractUserId(accessToken));

        return ResponseEntity.noContent().build();
    }
}

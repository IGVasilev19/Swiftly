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

    private ResponseCookie createRefreshCookie(String refreshToken, HttpServletRequest request) {
        boolean isSecure = request.isSecure() || "https".equalsIgnoreCase(request.getScheme());
        
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(isSecure)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Lax");

        return builder.build();
    }

    private ResponseCookie createDeleteCookie(HttpServletRequest request) {
        boolean isSecure = request.isSecure() || "https".equalsIgnoreCase(request.getScheme());
        
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(isSecure)
                .path("/")
                .maxAge(0)
                .sameSite("Lax");

        return builder.build();
    }

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
    public ResponseEntity<?> login(@RequestBody @Valid LogInRequest logInRequest, HttpServletRequest request)
    {
        try {
            LogInResponse response = LogInMapper.toLogInResponse(logInService.login(LogInMapper.toUser(logInRequest)));

            ResponseCookie refreshCookie = createRefreshCookie(response.refreshToken(), request);

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
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        
        if (cookies == null || cookies.length == 0) {
            ResponseCookie deleteCookie = createDeleteCookie(request);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                    .body(Map.of("error", "Missing cookies", "message", "No cookies found in request"));
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refresh_token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
        
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Missing refresh token", "message", "Refresh token cookie not found"));
        }

        com.swiftly.domain.User refreshUser = logInService.refreshToken(refreshToken);
        
        if (refreshUser == null) {
            ResponseCookie deleteCookie = createDeleteCookie(request);
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                    .body(Map.of("error", "Invalid or expired refresh token", "message", "Refresh token is invalid or expired"));
        }

        LogInResponse loginResponse = LogInMapper.toLogInResponse(refreshUser);
        
        ResponseCookie refreshCookie = createRefreshCookie(loginResponse.refreshToken(), request);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(Map.of("accessToken", loginResponse.accessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String authHeader,
            HttpServletRequest request,
            HttpServletResponse response) {

        ResponseCookie deleteCookie = createDeleteCookie(request);
        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        String accessToken = authHeader.replace("Bearer ", "");
        logInService.logout(jwtService.extractUserId(accessToken));

        return ResponseEntity.noContent().build();
    }

}

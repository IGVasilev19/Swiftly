package com.swiftly.web.auth.mapper;

import com.swiftly.domain.User;
import com.swiftly.web.auth.dto.LogInRequest;
import com.swiftly.web.auth.dto.LogInResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LogInMapperTest {

    @Test
    void toUser_WithValidRequest_ShouldMapCorrectly() {
        String email = "test@example.com";
        String password = "password123";

        LogInRequest request = new LogInRequest(email, password);

        User user = LogInMapper.toUser(request);

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
    }

    @Test
    void toLogInResponse_WithValidUser_ShouldMapCorrectly() {
        User user = new User();
        user.setAccessToken("access-token-123");
        user.setRefreshToken("refresh-token-456");

        LogInResponse response = LogInMapper.toLogInResponse(user);

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("access-token-123");
        assertThat(response.refreshToken()).isEqualTo("refresh-token-456");
    }

    @Test
    void toLogInResponse_WithNullTokens_ShouldMapCorrectly() {
        User user = new User();
        user.setAccessToken(null);
        user.setRefreshToken(null);

        LogInResponse response = LogInMapper.toLogInResponse(user);

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isNull();
        assertThat(response.refreshToken()).isNull();
    }
}


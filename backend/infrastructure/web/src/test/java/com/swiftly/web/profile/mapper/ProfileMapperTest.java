package com.swiftly.web.profile.mapper;

import com.swiftly.domain.Profile;
import com.swiftly.web.profile.dto.ProfileResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileMapperTest {

    @Test
    void toResponse_WithValidProfile_ShouldMapCorrectly() {
        Profile profile = new Profile(1, "John Doe", "+1234567890", "https://example.com/avatar.jpg");

        ProfileResponse response = ProfileMapper.toResponse(profile);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1);
        assertThat(response.fullName()).isEqualTo("John Doe");
        assertThat(response.phone()).isEqualTo("+1234567890");
        assertThat(response.avatarUrl()).isEqualTo("https://example.com/avatar.jpg");
    }

    @Test
    void toResponse_WithNullAvatarUrl_ShouldMapCorrectly() {
        Profile profile = new Profile(1, "John Doe", "+1234567890", null);

        ProfileResponse response = ProfileMapper.toResponse(profile);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1);
        assertThat(response.fullName()).isEqualTo("John Doe");
        assertThat(response.phone()).isEqualTo("+1234567890");
        assertThat(response.avatarUrl()).isNull();
    }
}


package com.swiftly.application.profile;

import com.swiftly.application.profile.port.inbound.ProfileUseCase;
import com.swiftly.application.profile.port.outbound.ProfilePort;
import com.swiftly.domain.Profile;
import com.swiftly.domain.User;
import com.swiftly.domain.enums.user.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileUseCase profileUseCase;

    @Mock
    ProfilePort profilePort;

    @InjectMocks
    private ProfileService profileService;

    @Test
    void getById_profileExists() {
        // Arrange
        Integer profileId = 42;

        Profile mockProfile = new Profile(
                "Ada Lovelace",
                "+31612345678",
                "123 Babbage Street",
                "London",
                "UK",
                "EC1A 1BB"
        );

        when(profilePort.findById(profileId)).thenReturn(Optional.of(mockProfile));

        ProfileService profileService = new ProfileService(profilePort); // make sure this isn't null!

        // Act
        Optional<Profile> result = profileService.getById(profileId);

        // Assert
        assertNotNull(result);
        assertEquals("Ada Lovelace", result.get().getFullName());
        verify(profilePort).findById(profileId);
    }


    @Test
    void getById_profileNotFound_throwsException() {
        Integer profileId = 999;

        // Arrange: return empty to simulate "not found"
        when(profilePort.findById(profileId)).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            profileService.getById(profileId);
        });

        assertEquals("Profile not found", ex.getMessage());
        verify(profilePort).findById(profileId);
    }
}

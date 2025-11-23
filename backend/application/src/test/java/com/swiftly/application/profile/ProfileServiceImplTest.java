package com.swiftly.application.profile;

import com.swiftly.application.profile.port.inbound.ProfileService;
import com.swiftly.application.profile.port.outbound.ProfileRepository;
import com.swiftly.domain.Profile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("unit")
@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @Mock
    private ProfileService profileService;

    @Mock
    ProfileRepository profileRepository;

    @InjectMocks
    private ProfileServiceImpl profileServiceImpl;

    @Test
    void getById_profileExists() {
        // Arrange
        Integer profileId = 42;

        Profile mockProfile = new Profile(
                "Ada Lovelace",
                "+31612345678"
        );

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(mockProfile));

        ProfileServiceImpl profileServiceImpl = new ProfileServiceImpl(profileRepository); // make sure this isn't null!

        // Act
        Optional<Profile> result = profileServiceImpl.getById(profileId);

        // Assert
        assertNotNull(result);
        assertEquals("Ada Lovelace", result.get().getFullName());
        verify(profileRepository).findById(profileId);
    }


    @Test
    void getById_profileNotFound_throwsException() {
        Integer profileId = 999;

        // Arrange: return empty to simulate "not found"
        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            profileServiceImpl.getById(profileId);
        });

        assertEquals("Profile not found", ex.getMessage());
        verify(profileRepository).findById(profileId);
    }
}

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
        Integer profileId = 42;

        Profile mockProfile = new Profile(
                "Ada Lovelace",
                "+31612345678"
        );

        when(profileRepository.findById(profileId)).thenReturn(mockProfile);

        ProfileServiceImpl profileServiceImpl = new ProfileServiceImpl(profileRepository);

        Profile result = profileServiceImpl.getById(profileId);

        assertNotNull(result);
        assertEquals("Ada Lovelace", result.getFullName());
        verify(profileRepository).findById(profileId);
    }


    @Test
    void getById_profileNotFound_throwsException() {
        Integer profileId = 999;

        when(profileRepository.findById(profileId)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            profileServiceImpl.getById(profileId);
        });

        assertEquals("Profile not found", ex.getMessage());
        verify(profileRepository).findById(profileId);
    }
}

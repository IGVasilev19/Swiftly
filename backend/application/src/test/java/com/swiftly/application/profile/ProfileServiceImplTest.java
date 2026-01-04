package com.swiftly.application.profile;

import com.swiftly.application.profile.port.outbound.ProfileRepository;
import com.swiftly.domain.Profile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit")
class ProfileServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;

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

        Profile result = profileServiceImpl.getById(profileId);

        assertNotNull(result);
        assertEquals("Ada Lovelace", result.getFullName());
        verify(profileRepository).findById(profileId);
    }

    @Test
    void getById_profileNotFound_returnsNull() {
        Integer profileId = 999;

        when(profileRepository.findById(profileId)).thenReturn(null);

        Profile result = profileServiceImpl.getById(profileId);

        assertNull(result);
        verify(profileRepository).findById(profileId);
    }
}

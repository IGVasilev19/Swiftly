package com.swiftly.application.profile;

import com.swiftly.application.profile.port.inbound.ProfileUseCase;
import com.swiftly.domain.Profile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileUseCase profileUseCase;

    @InjectMocks
    private ProfileService profileService;

    @Test
    void getById_profileExists() {
        // Arrange
        Random random = new Random();
        Integer id = random.nextInt(10000);
        Profile expectedProfile = new Profile(id);

        when(profileUseCase.getById(id)).thenReturn(Optional.of(expectedProfile));

        // Act
        Optional<Profile> actualProfile = profileService.getById(id);

        // Assert
        assertEquals(expectedProfile, actualProfile);
        verify(profileUseCase).getById(id);
    }

    @Test
    void getById_profileNotFound_returnsNull() {
        // Arrange
        Random random = new Random();
        Integer id = random.nextInt(10000);
        when(profileUseCase.getById(id)).thenReturn(Optional.empty());


        // Act
        Optional<Profile> result = profileService.getById(id);

        // Assert
        assertNull(result);
        verify(profileUseCase).getById(id);
    }

    @Test
    void getById_profileNotFound_throwsException() {
        // Arrange
        Random random = new Random();
        Integer id = random.nextInt(10000);

        when(profileUseCase.getById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> profileService.getById(id));
        verify(profileUseCase).getById(id);
    }
}

package com.swiftly.application.profile;

import com.swiftly.application.profile.port.inbound.ProfileService;
import com.swiftly.application.profile.port.outbound.ProfileRepository;
import com.swiftly.domain.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;


    public Profile getById(Integer id)
    {
        return profileRepository.findById(id);
    }
}

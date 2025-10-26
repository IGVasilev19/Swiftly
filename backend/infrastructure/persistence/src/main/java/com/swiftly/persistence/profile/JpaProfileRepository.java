package com.swiftly.persistence.profile;


import com.swiftly.domain.Profile;
import com.swiftly.persistence.entities.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProfileRepository extends JpaRepository<ProfileEntity, Integer> {

}

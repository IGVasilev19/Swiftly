package com.swiftly.persistence.profile;


import com.swiftly.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProfileRepository extends JpaRepository<Profile, Integer> {

}

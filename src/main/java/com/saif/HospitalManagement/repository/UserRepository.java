package com.saif.HospitalManagement.repository;

import com.saif.HospitalManagement.entity.User;
import com.saif.HospitalManagement.entity.type.AuthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByProviderIdAndProviderType(String providerId, AuthProviderType providerType);
}

package com.saif.HospitalManagement.repository;

import com.saif.HospitalManagement.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
}
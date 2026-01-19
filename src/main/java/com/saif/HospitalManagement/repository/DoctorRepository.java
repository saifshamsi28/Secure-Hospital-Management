package com.saif.HospitalManagement.repository;

import com.saif.HospitalManagement.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}

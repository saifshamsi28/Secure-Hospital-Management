package com.saif.HospitalManagement.repository;

import com.saif.HospitalManagement.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
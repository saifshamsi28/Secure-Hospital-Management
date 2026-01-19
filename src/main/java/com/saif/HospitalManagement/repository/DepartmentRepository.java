package com.saif.HospitalManagement.repository;

import com.saif.HospitalManagement.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
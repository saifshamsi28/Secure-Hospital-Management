package com.saif.HospitalManagement.repository;

import com.saif.HospitalManagement.dto.BloodGroupCountResponseEntity;
import com.saif.HospitalManagement.entity.Patient;
import com.saif.HospitalManagement.entity.type.BloodGroupType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByName(String name);
    List<Patient> findByBirthDateOrEmail(LocalDate birthDate, String email);

    List<Patient> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);

    List<Patient> findByNameContainingOrderByIdDesc(String query);

    @Query("SELECT p FROM Patient p where p.bloodGroup = ?1")
    List<Patient> findByBloodGroup(@Param("bloodGroup") BloodGroupType bloodGroup);

    @Query("select p from Patient p where p.birthDate > :birthDate")
    List<Patient> findByBornAfterDate(@Param("birthDate") LocalDate birthDate);

    @Query("select new com.saif.HospitalManagement.dto.BloodGroupCountResponseEntity(p.bloodGroup," +
            " Count(p)) from Patient p group by p.bloodGroup")  //use of projection for industry level query for optimization
//    List<Object[]> countEachBloodGroupType();
    List<BloodGroupCountResponseEntity> countEachBloodGroupType();

    @Query(value = "select * from patient", nativeQuery = true) //native query true means hibernate will run this query same to same
    Page<Patient> findAllPatients(Pageable pageable);

    @Transactional //needed when changing something in database
    @Modifying // because you have to explicitly tell to hibernate that this query is for updating the database
    @Query("UPDATE Patient p SET p.name = :name where p.id = :id")
    int updateNameWithId(@Param("name") String name, @Param("id") Long id);

//    @Query("SELECT p FROM Patient p LEFT JOIN FETCH p.appointments a LEFT JOIN FETCH a.doctor") //but very unoptimised query, less joins -> more optimised
    @Query("SELECT p FROM Patient p LEFT JOIN FETCH p.appointments") //still doctor will be fetched eagerly until we make the fetchType lazy in Appointment with Doctor
    List<Patient> findAllPatientsWithAppointments();



}

package com.saif.HospitalManagement;

import com.saif.HospitalManagement.entity.Appointment;
import com.saif.HospitalManagement.entity.Patient;
import com.saif.HospitalManagement.repository.PatientRepository;
import com.saif.HospitalManagement.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@SpringBootTest
public class PatientTests {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    @Test
    public void testPatientRepository() {

//        List<Patient> patientList = patientRepository.findAll(); // but this created N+1 query problem
        //means how much row we got from the 1st query that created separate query for each row means
        // N is the row got from 1st query

        List<Patient> patientList=patientRepository.findAllPatientsWithAppointments(); // it stills fetch the doctor eagerly due to Appointment have ManyToOne relation with Doctor
        //print the patients
        System.out.println(patientList);
        System.out.println("one thing more verifying");
        System.out.println(patientList.getFirst().getAppointments().getFirst().getDoctor());


//        Patient p1 = new Patient();
//        patientRepository.save(p1);
    }

//    @Test
//    public void testTransactionMethods() {
////        Patient patient = patientService.getPatientById(1L);
//
////        Patient patient = patientRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException("Patient not " +
////                "found with id: 1"));
//
////        Patient patient = patientRepository.findByName("Diya Patel");
//
////        List<Patient> patientList = patientRepository.findByBirthDateOrEmail(LocalDate.of(1988, 3, 15), "diya" +
////                ".patel@example.com");
//
////        List<Patient> patientList = patientRepository.findByBornAfterDate(LocalDate.of(1993, 3, 14));
//
////        Page<Patient> patientList = patientRepository.findAllPatients(PageRequest.of(1, 2, Sort.by("name")));
//        Page<Patient> patientList = patientRepository.findAllPatients(PageRequest.of(1, 2, Sort.by("name")));
//
//        for(Patient patient: patientList) {
//            System.out.println(patient);
//        }
////
////        List<Object[]> bloodGroupList = patientRepository.countEachBloodGroupType();
////        for(Object[] objects: bloodGroupList) {
////            System.out.println(objects[0] +" "+ objects[1]);
////        }
//
////        int rowsUpdated = patientRepository.updateNameWithId("Arav Sharma", 1L);
////        System.out.println(rowsUpdated);
//
////        List<BloodGroupCountResponseEntity> bloodGroupList = patientRepository.countEachBloodGroupType();
////        for(BloodGroupCountResponseEntity bloodGroupCountResponse: bloodGroupList) {
////            System.out.println(bloodGroupCountResponse);
////        }
//    }
}

























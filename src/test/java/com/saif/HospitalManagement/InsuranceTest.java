package com.saif.HospitalManagement;

import com.saif.HospitalManagement.entity.Insurance;
import com.saif.HospitalManagement.entity.Patient;
import com.saif.HospitalManagement.service.InsuranceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Date;

@SpringBootTest
public class InsuranceTest {

    @Autowired
    private InsuranceService insuranceService;

    @Test
    public void insuranceCreationTest(){
        Insurance insurance=Insurance.builder()
                .policyNumber("SBI_1234")
                .provider("SBI BANK")
                .validUntil(LocalDate.of(2030,10,10))
                .build();

        Patient patient=insuranceService.assignInsuranceToPatient(insurance, 2L);
        System.out.println(patient);

        //now removing the insurance from patient

        var newPatient=insuranceService.removeInsuranceFromPatient(patient.getId());
        System.out.println(newPatient);
    }
}

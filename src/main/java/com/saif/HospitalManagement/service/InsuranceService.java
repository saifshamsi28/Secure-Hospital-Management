package com.saif.HospitalManagement.service;

import com.saif.HospitalManagement.entity.Insurance;
import com.saif.HospitalManagement.entity.Patient;
import com.saif.HospitalManagement.repository.InsuranceRepository;
import com.saif.HospitalManagement.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public Patient assignInsuranceToPatient(Insurance insurance, Long patientId){
        Patient patient=patientRepository.findById(patientId).
                orElseThrow(()-> new EntityNotFoundException( "Patient not found with id: "+patientId));

        patient.setInsurance(insurance); // first assign to patient because its Owning side

        insurance.setPatient(patient); // for bidirectional consistency maintenance

        return patient;
    }

    @Transactional
    public Patient removeInsuranceFromPatient(Long patientId){
        Patient patient=patientRepository.findById(patientId).
                orElseThrow(()-> new EntityNotFoundException( "Patient not found with id: "+patientId));

        patient.setInsurance(null); // this will update the patient as we are in transactional context because dirty check will be performed
        return patient;
    }

}

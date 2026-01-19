package com.saif.HospitalManagement.service;

import com.saif.HospitalManagement.dto.PatientResponseDto;
import com.saif.HospitalManagement.dto.PatientUpdateDto;
import com.saif.HospitalManagement.entity.Patient;
import com.saif.HospitalManagement.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;
    private final String CACHE_PATIENT="patients";

    @Transactional
    @Cacheable(cacheNames = CACHE_PATIENT,key = "#patientId")
    public PatientResponseDto getPatientById(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new EntityNotFoundException("Patient Not " +
                "Found with id: " + patientId));
        return modelMapper.map(patient, PatientResponseDto.class);
    }

    @Transactional
    @CachePut(cacheNames = CACHE_PATIENT,key = "#result.id") // #result.id because 'result' is reserved keyword while id is present as unique identifier in PatientResponseDto class
    public PatientResponseDto updatePatient(PatientUpdateDto patientUpdateDto){
        Patient patient = patientRepository.findById(patientUpdateDto.getPatientId()).orElseThrow(() -> new EntityNotFoundException("Patient Not " +
                "Found with id: " + patientUpdateDto.getPatientId()));

        patient.setName(patientUpdateDto.getName());
        patient.setEmail(patientUpdateDto.getEmail());
        return modelMapper.map(patient, PatientResponseDto.class);
    }

    public List<PatientResponseDto> getAllPatients(Integer pageNumber, Integer pageSize) {
        return patientRepository.findAllPatients(PageRequest.of(pageNumber, pageSize))
                .stream()
                .map(patient -> modelMapper.map(patient, PatientResponseDto.class))
                .collect(Collectors.toList());
    }
}

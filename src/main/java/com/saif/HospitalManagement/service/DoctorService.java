package com.saif.HospitalManagement.service;

import com.saif.HospitalManagement.dto.DoctorResponseDto;
import com.saif.HospitalManagement.dto.OnboardDoctorRequestDto;
import com.saif.HospitalManagement.entity.Doctor;
import com.saif.HospitalManagement.entity.User;
import com.saif.HospitalManagement.entity.type.Role;
import com.saif.HospitalManagement.repository.DoctorRepository;
import com.saif.HospitalManagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public List<DoctorResponseDto> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(doctor -> modelMapper.map(doctor, DoctorResponseDto.class))
                .collect(Collectors.toList());
    }


    @Transactional
    public DoctorResponseDto onBoardNewDoctor(OnboardDoctorRequestDto onboardDoctorRequestDto) {
        User user=userRepository.findById(onboardDoctorRequestDto.getUserId()).orElseThrow();

        if(doctorRepository.existsById(onboardDoctorRequestDto.getUserId())){
            throw new IllegalArgumentException("Already a doctor");
        }

        Doctor doctor=Doctor.builder()
                .specialization(onboardDoctorRequestDto.getSpecialization())
                .name(onboardDoctorRequestDto.getName())
                .user(user)
                .build();
        user.getRoles().add(Role.DOCTOR);
        return modelMapper.map(doctorRepository.save(doctor), DoctorResponseDto.class);
    }
}

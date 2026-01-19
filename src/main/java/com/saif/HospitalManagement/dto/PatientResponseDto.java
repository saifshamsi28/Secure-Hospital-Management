package com.saif.HospitalManagement.dto;

import com.saif.HospitalManagement.entity.type.BloodGroupType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class PatientResponseDto implements Serializable {
    private Long id;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private BloodGroupType bloodGroup;
}

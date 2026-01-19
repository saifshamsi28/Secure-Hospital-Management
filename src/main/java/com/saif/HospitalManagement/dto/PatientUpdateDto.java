package com.saif.HospitalManagement.dto;

import lombok.Data;

@Data
public class PatientUpdateDto {

    private Long patientId;
    private String name;
    private String email;
}

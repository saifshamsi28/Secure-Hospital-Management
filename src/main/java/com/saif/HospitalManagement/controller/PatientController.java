package com.saif.HospitalManagement.controller;

import com.saif.HospitalManagement.dto.AppointmentResponseDto;
import com.saif.HospitalManagement.dto.CreateAppointmentRequestDto;
import com.saif.HospitalManagement.dto.PatientResponseDto;
import com.saif.HospitalManagement.dto.PatientUpdateDto;
import com.saif.HospitalManagement.service.AppointmentService;
import com.saif.HospitalManagement.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponseDto> createNewAppointment(@RequestBody CreateAppointmentRequestDto createAppointmentRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.createNewAppointment(createAppointmentRequestDto));
    }

    @GetMapping("id/{id}")
    private ResponseEntity<PatientResponseDto> getPatientProfile(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PatchMapping("/update")
    private ResponseEntity<PatientResponseDto> updatePatient(@RequestBody PatientUpdateDto patientUpdateDto) {
        return ResponseEntity.ok(patientService.updatePatient(patientUpdateDto));
    }

}

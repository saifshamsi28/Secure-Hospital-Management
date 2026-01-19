package com.saif.HospitalManagement.service;

import com.saif.HospitalManagement.dto.AppointmentResponseDto;
import com.saif.HospitalManagement.dto.CreateAppointmentRequestDto;
import com.saif.HospitalManagement.entity.Appointment;
import com.saif.HospitalManagement.entity.Doctor;
import com.saif.HospitalManagement.entity.Patient;
import com.saif.HospitalManagement.repository.AppointmentRepository;
import com.saif.HospitalManagement.repository.DoctorRepository;
import com.saif.HospitalManagement.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Secured("ROLE_PATIENT")
    public AppointmentResponseDto createNewAppointment(CreateAppointmentRequestDto createAppointmentRequestDto) {
        Long doctorId = createAppointmentRequestDto.getDoctorId();
        Long patientId = createAppointmentRequestDto.getPatientId();

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + patientId));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + doctorId));
        Appointment appointment = Appointment.builder()
                .reason(createAppointmentRequestDto.getReason())
                .appointmentTime(createAppointmentRequestDto.getAppointmentTime())
                .build();

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        patient.getAppointments().add(appointment); // to maintain bidirectional consistency so that we can get patient details from appointment
//        doctor.getAppointments().add(appointment); // to maintain bidirectional consistency so that we can get patient details from appointment


        appointment = appointmentRepository.save(appointment);
        return modelMapper.map(appointment, AppointmentResponseDto.class);
    }

    @Transactional
    @PreAuthorize("hasAuthority('appointment:write') or doctorId==authentication.principal.id")
    public Appointment reAssignAppointmentToAnotherDoctor(Long appointmentId, Long doctorId) {
        Appointment appointment=appointmentRepository.findById(appointmentId)
                .orElseThrow(()->new EntityNotFoundException("No booked Appointment with the id: "+appointmentId));

        Doctor doctor=doctorRepository.findById(doctorId)
                .orElseThrow(()->new EntityNotFoundException("No doctor found with the id: "+doctorId));


        appointment.setDoctor(doctor); // this will automatically call the update, because it is dirty

        doctor.getAppointments().add(appointment); //just to maintain bidirectional consistency
        //return appointmentRepository.save(appointment); //no need to save again, as we are in transactional context so after
        // all operations dirty checking will be performed and it will be found
        // that appointment has change so it will automatically update the appointment by calling update


        return appointment;
    }

    @PreAuthorize("hasRole('ADMIN') OR (HasRole('DOCTOR') AND doctorId==authentication.principal.id)")
    public List<AppointmentResponseDto> getAllAppointmentsOfDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();

        return doctor.getAppointments()
                .stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentResponseDto.class))
                .collect(Collectors.toList());
    }
}

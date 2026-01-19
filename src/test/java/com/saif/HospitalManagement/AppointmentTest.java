package com.saif.HospitalManagement;

import com.saif.HospitalManagement.entity.Appointment;
import com.saif.HospitalManagement.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class AppointmentTest {

    @Autowired
    private AppointmentService appointmentService;

    @Test
    public void testCreateNewAppointment(){
        Appointment appointment=Appointment.builder()
                .appointmentTime(LocalDateTime.of(2026, 1,11,15, 0))
                .reason("Checkup for Cancer")
                .build();

//        var newAppointment=appointmentService.createNewAppointment(appointment,1L,2L);
//        System.out.println(newAppointment);


        // now testing the re-assignment of appointment to another doctor

//        var newReAssignedAppointment=appointmentService.reAssignAppointmentToAnotherDoctor(newAppointment.getId(), 2L);
//        System.out.println(newReAssignedAppointment);

    }
}

package com.saif.HospitalManagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String specialization;

    @Column( unique = true, length = 100)
    private String email;

    @OneToOne
    @MapsId
    private User user;

    @ManyToMany(mappedBy = "doctors")
    @ToString.Exclude
    private Set<Department> departments=new HashSet<>();

    @OneToMany(mappedBy = "doctor")
    @ToString.Exclude //to prevent auto fetching when doctor is called so there appointments should not call automatically
    private List<Appointment> appointments=new ArrayList<>();
}

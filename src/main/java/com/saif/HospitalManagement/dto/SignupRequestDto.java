package com.saif.HospitalManagement.dto;

import com.saif.HospitalManagement.entity.type.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {
    private String username;
    private String password;
    private String name;
    Set<Role> roles=new HashSet<>();
}

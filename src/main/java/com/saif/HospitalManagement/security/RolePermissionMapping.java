package com.saif.HospitalManagement.security;

import com.saif.HospitalManagement.entity.Patient;
import com.saif.HospitalManagement.entity.type.PermissionType;
import com.saif.HospitalManagement.entity.type.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.saif.HospitalManagement.entity.type.PermissionType.*;
import static com.saif.HospitalManagement.entity.type.Role.*;

public class RolePermissionMapping {

    private static final Map<Role, Set<PermissionType>> map=Map.of(
            PATIENT,Set.of(PATIENT_READ,APPOINTMENT_READ,APPOINTMENT_WRITE),
            DOCTOR, Set.of(PATIENT_READ,APPOINTMENT_READ,APPOINTMENT_WRITE,APPOINTMENT_DELETE),
            ADMIN,Set.of(PATIENT_READ,PATIENT_WRITE,APPOINTMENT_READ,APPOINTMENT_WRITE,APPOINTMENT_DELETE,USER_MANAGE,REPORT_VIEW)
    );

    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(Role role){
        return map.get(role).stream()
                .map(permission-> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}

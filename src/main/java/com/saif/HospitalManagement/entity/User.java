package com.saif.HospitalManagement.entity;

import com.saif.HospitalManagement.entity.type.AuthProviderType;
import com.saif.HospitalManagement.entity.type.Role;
import com.saif.HospitalManagement.security.RolePermissionMapping;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "app_user",indexes = {
        @Index(name = "idx_provider_id_provider_type",columnList = "providerId,providerType")
})
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    Set<Role> roles=new HashSet<>();

    private String providerId;

    @Enumerated(EnumType.STRING)
    private AuthProviderType providerType;

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles.stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
//                .collect(Collectors.toSet()); // TO DEFINE ONLY ROLE BASE ACESS

        Set<SimpleGrantedAuthority> authorities=new HashSet<>();

        roles.forEach(role -> {
            Set<SimpleGrantedAuthority> permissions= RolePermissionMapping.getAuthoritiesForRole(role);
            authorities.addAll(permissions);
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.name()));
        });
        return authorities;
    }

}

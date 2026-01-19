package com.saif.HospitalManagement.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.PublicKey;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration){
        return configuration.getAuthenticationManager();
    }

    //  I can put this anywhere as this is bean
    //  we just have to annotate @Bean wherever we define this
    //  means we can define this in WebSecurityConfig or in any other class

//    @Bean
//    UserDetailsService userDetailsService() {
//        UserDetails user1 = User.withUsername("saif")
//                .password(passwordEncoder.encode("1234"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user2=User.withUsername("sarfraz")
//                .password(passwordEncoder.encode("12"))
//                .roles("PATIENT")
//                .build();
//
//        return new InMemoryUserDetailsManager(user1,user2);
//    }
}

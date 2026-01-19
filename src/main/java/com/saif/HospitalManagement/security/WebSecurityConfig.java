package com.saif.HospitalManagement.security;

import com.saif.HospitalManagement.entity.type.PermissionType;
import com.saif.HospitalManagement.entity.type.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

import static com.saif.HospitalManagement.entity.type.PermissionType.*;
import static com.saif.HospitalManagement.entity.type.Role.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableMethodSecurity
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){

        //## Point -> 1
//        httpSecurity.formLogin(Customizer.withDefaults()); // this will bypass the spring security and public the all endpoints

        //## Point -> 2
//        httpSecurity
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/public/**").permitAll() // this will make public to these endpoints
//                        .requestMatchers("/admin/**").authenticated()) // this will make private to these endpoints and authenticate them before serving request
//                .formLogin(Customizer.withDefaults());

        // ## Point -> 3

        httpSecurity
                .csrf(csrfConfig-> csrfConfig.disable())
                .sessionManagement(customizer-> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/public/**","/auth/**").permitAll()
                                .requestMatchers("/doctors/**").hasAnyRole(DOCTOR.name(), ADMIN.name()) //role base access
                                .requestMatchers("/admin/**").hasRole(ADMIN.name()) // role base access
                                .requestMatchers(HttpMethod.DELETE,"/admin/**").hasAnyAuthority(APPOINTMENT_DELETE.name(),USER_MANAGE.name())
                                .anyRequest().authenticated()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/doctors/**").hasAnyRole("DOCTOR","ADMIN")//here role also be checked

                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oAuth2-> oAuth2.
                        failureHandler((request, response, exception) -> {
                    log.error("OAuth2 error: {} ",exception.getMessage());
                            handlerExceptionResolver.resolveException(request,response,null,exception);
                        })
                        .successHandler(oAuth2SuccessHandler)
                ).exceptionHandling(exceptionHandlingConfigurer->
                        exceptionHandlingConfigurer.accessDeniedHandler((request, response, accessDeniedException) -> {
                            handlerExceptionResolver.resolveException(request,response,null,accessDeniedException);
                        }));

//                .formLogin(Customizer.withDefaults()); //commented this to remove the default login form as
                                                         //we want login form at frontend, so we will handle things about login form
        return httpSecurity.build();
    }

}

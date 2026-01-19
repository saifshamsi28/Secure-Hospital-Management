package com.saif.HospitalManagement.security;

import com.saif.HospitalManagement.dto.LoginRequestDto;
import com.saif.HospitalManagement.dto.LoginResponseDto;
import com.saif.HospitalManagement.dto.SignupRequestDto;
import com.saif.HospitalManagement.dto.SignupResponseDto;
import com.saif.HospitalManagement.entity.Patient;
import com.saif.HospitalManagement.entity.User;
import com.saif.HospitalManagement.entity.type.AuthProviderType;
import com.saif.HospitalManagement.entity.type.Role;
import com.saif.HospitalManagement.repository.PatientRepository;
import com.saif.HospitalManagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),loginRequestDto.getPassword())
        );

        User user= (User) authentication.getPrincipal();

        String token=authUtil.generateAccessToken(user);

        return new LoginResponseDto(token, user.getId());

    }


    public User signupInternal(SignupRequestDto signupRequestDto,
                               AuthProviderType authProviderType,
                               String providerId){
        User user=userRepository.
                findByUsername(signupRequestDto.getUsername()).orElse(null);

        if(user != null){
            throw new IllegalArgumentException("User already exists");
        }

        user=User.builder()
                .username(signupRequestDto.getUsername())
                .providerType(authProviderType)
                .providerId(providerId)
//                .roles(Set.of(Role.PATIENT)) // standard practice, then after admin decide the role
                .roles(signupRequestDto.getRoles()) // just for testing
                .build();

        if(authProviderType == AuthProviderType.EMAIL){
            user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        }

        user= userRepository.save(user);

        Patient patient=Patient.builder()
                .name(signupRequestDto.getName())
                .email(signupRequestDto.getUsername())
                .user(user)
                .build();

        patientRepository.save(patient);

        return user;
    }

    // request hit from login controller
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        User user= signupInternal(signupRequestDto, AuthProviderType.EMAIL,null);

        return new SignupResponseDto(user.getId(), user.getUsername());
    }

    @Transactional
    public ResponseEntity<LoginResponseDto> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {

        // Step 1: fetch the provider type and provider id
        // Step 2: check user already exists or save the provider type and id info with user
        // Step 3: if the user has account: direct login
        // Step 4: otherwise: first signup then login

        //Step 1:-
        AuthProviderType providerType=authUtil.getProviderTypeFromRegistrationId(registrationId);
        String providerId= authUtil.determineProviderIdFromOAuth2User(oAuth2User, registrationId);

        //Step 2:-
        User user=userRepository.findByProviderIdAndProviderType(providerId,providerType).orElse(null);
        String email=oAuth2User.getAttribute("email");
        String name=oAuth2User.getAttribute("name");

        User emailUser=userRepository.findByUsername(email).orElse(null);

        if(user == null && emailUser == null){
            //signup flow
            String username=authUtil.determineUsernameFromOAuth2User(oAuth2User,registrationId,providerId);
            user=signupInternal(new SignupRequestDto(username,null,name,Set.of(Role.PATIENT)),providerType,providerId);
        } else if (user !=null ) {
            if(email !=null && !email.isBlank() && !email.equals(user.getUsername())){
                user.setUsername(email);
                userRepository.save(user);
            }
        }else {
            throw new BadCredentialsException("This email is already register with provider: "+emailUser.getProviderType());
        }

        //Step 3: if the user has account: direct login
        LoginResponseDto loginResponseDto=new LoginResponseDto(authUtil.generateAccessToken(user),user.getId());
        return ResponseEntity.ok(loginResponseDto);
    }
}

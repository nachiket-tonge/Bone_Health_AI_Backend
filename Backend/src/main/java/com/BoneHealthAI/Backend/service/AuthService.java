package com.BoneHealthAI.Backend.service;



import com.BoneHealthAI.Backend.dto.*;
import com.BoneHealthAI.Backend.entity.User;
import com.BoneHealthAI.Backend.repository.UserRepository;
import com.BoneHealthAI.Backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.BoneHealthAI.Backend.entity.Clinic;
import com.BoneHealthAI.Backend.repository.ClinicRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ClinicRepository clinicRepository;


    @Transactional
    public AuthResponse signup(SignupRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email already registered"
            );
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(User.Role.CLINIC_ADMIN);

        userRepository.save(user);

        Clinic clinic = new Clinic();
        clinic.setName(request.getClinicName().trim());
        clinic.setAddress(request.getAddress().trim());
        clinic.setPhone(request.getPhone().trim());
        clinic.setUser(user);

        clinicRepository.save(clinic);

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getName(),
                user.getRole()
        );
    }


    public AuthResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(email);
        return new AuthResponse(token, user.getEmail(), user.getName(), user.getRole());
    }
}

package com.BoneHealthAI.Backend.controller;

import com.BoneHealthAI.Backend.entity.Clinic;
import com.BoneHealthAI.Backend.entity.User;
import com.BoneHealthAI.Backend.repository.ClinicRepository;
import com.BoneHealthAI.Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Map<String, Object> profile = new HashMap<>();

        profile.put("id", user.getId());
        profile.put("name", user.getName());
        profile.put("email", user.getEmail());
        profile.put("role", user.getRole());

        if (user.getRole() == User.Role.CLINIC_ADMIN) {
            Clinic clinic = clinicRepository.findByUserId(user.getId())
                    .orElseThrow(() ->
                            new RuntimeException("Clinic not found"));

            profile.put("clinicId", clinic.getId());
            profile.put("clinicName", clinic.getName());
            profile.put("address", clinic.getAddress());
            profile.put("phone", clinic.getPhone());
        }

        return ResponseEntity.ok(profile);
    }
}
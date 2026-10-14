package com.BoneHealthAI.Backend.controller;

import com.BoneHealthAI.Backend.dto.PatientRequest;
import com.BoneHealthAI.Backend.dto.PatientResponse;
import com.BoneHealthAI.Backend.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public List<PatientResponse> searchPatients(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String search) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        return patientService.searchPatients(userDetails.getUsername(), search);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientResponse createPatient(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PatientRequest request) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        return patientService.createPatient(userDetails.getUsername(), request);
    }

    @GetMapping("/{id}")
    public PatientResponse getPatient(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        return patientService.getPatient(userDetails.getUsername(), id);
    }
}

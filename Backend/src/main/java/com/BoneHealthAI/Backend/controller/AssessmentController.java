package com.BoneHealthAI.Backend.controller;

import com.BoneHealthAI.Backend.dto.AssessmentCreateRequest;
import com.BoneHealthAI.Backend.dto.AssessmentResponse;
import com.BoneHealthAI.Backend.dto.DashboardSummaryResponse;
import com.BoneHealthAI.Backend.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;

    @PostMapping("/api/assessments")
    @ResponseStatus(HttpStatus.CREATED)
    public AssessmentResponse create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AssessmentCreateRequest request) {
        return assessmentService.create(requireEmail(userDetails), request);
    }

    @GetMapping("/api/assessments")
    public List<AssessmentResponse> list(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long patientId,
            @RequestParam(defaultValue = "20") int limit) {
        return assessmentService.list(requireEmail(userDetails), patientId, limit);
    }

    @GetMapping("/api/dashboard/summary")
    public DashboardSummaryResponse dashboard(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "5") int recentLimit) {
        return assessmentService.dashboard(requireEmail(userDetails), recentLimit);
    }

    private String requireEmail(UserDetails userDetails) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        return userDetails.getUsername();
    }
}

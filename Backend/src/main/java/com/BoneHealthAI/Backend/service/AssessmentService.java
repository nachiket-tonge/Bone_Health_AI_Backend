package com.BoneHealthAI.Backend.service;

import com.BoneHealthAI.Backend.dto.AssessmentCreateRequest;
import com.BoneHealthAI.Backend.dto.AssessmentResponse;
import com.BoneHealthAI.Backend.dto.DashboardSummaryResponse;
import com.BoneHealthAI.Backend.entity.Assessment;
import com.BoneHealthAI.Backend.entity.Clinic;
import com.BoneHealthAI.Backend.entity.Patient;
import com.BoneHealthAI.Backend.entity.User;
import com.BoneHealthAI.Backend.repository.AssessmentRepository;
import com.BoneHealthAI.Backend.repository.ClinicRepository;
import com.BoneHealthAI.Backend.repository.PatientRepository;
import com.BoneHealthAI.Backend.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;
    private final ObjectMapper objectMapper;

    public AssessmentResponse create(String email, AssessmentCreateRequest request) {
        Clinic clinic = getClinic(email);

        if (request == null || request.getPatientId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "patientId is required");
        }
        if (request.getAssessmentType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "assessmentType is required");
        }
        if (request.getResultSummary() == null || request.getResultSummary().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "resultSummary is required");
        }
        if (request.getResultSummary().trim().length() > 500) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "resultSummary must be 500 characters or fewer");
        }

        Patient patient = patientRepository.findByIdAndClinicId(request.getPatientId(), clinic.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Patient not found in your clinic"));

        String detailsJson = null;
        if (request.getResultDetails() != null && !request.getResultDetails().isEmpty()) {
            try {
                detailsJson = objectMapper.writeValueAsString(request.getResultDetails());
            } catch (JsonProcessingException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Result details are not valid JSON");
            }
        }

        Assessment assessment = new Assessment();
        assessment.setClinic(clinic);
        assessment.setPatient(patient);
        assessment.setAssessmentType(request.getAssessmentType());
        assessment.setResultSummary(request.getResultSummary().trim());
        assessment.setResultDetailsJson(detailsJson);

        return new AssessmentResponse(assessmentRepository.save(assessment), objectMapper);
    }

    @Transactional(readOnly = true)
    public List<AssessmentResponse> list(String email, Long patientId, int limit) {
        Clinic clinic = getClinic(email);
        int safeLimit = Math.max(1, Math.min(limit, 100));
        var page = PageRequest.of(0, safeLimit);

        if (patientId != null) {
            // Validate ownership before querying assessment history.
            patientRepository.findByIdAndClinicId(patientId, clinic.getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Patient not found in your clinic"));
            return assessmentRepository
                    .findByClinicIdAndPatientIdOrderByPerformedAtDesc(clinic.getId(), patientId, page)
                    .map(a -> new AssessmentResponse(a, objectMapper))
                    .getContent();
        }

        return assessmentRepository.findByClinicIdOrderByPerformedAtDesc(clinic.getId(), page)
                .map(a -> new AssessmentResponse(a, objectMapper))
                .getContent();
    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponse dashboard(String email, int recentLimit) {
        Clinic clinic = getClinic(email);
        int safeLimit = Math.max(1, Math.min(recentLimit, 20));
        List<AssessmentResponse> recent = assessmentRepository
                .findByClinicIdOrderByPerformedAtDesc(clinic.getId(), PageRequest.of(0, safeLimit))
                .map(a -> new AssessmentResponse(a, objectMapper))
                .getContent();

        return new DashboardSummaryResponse(
                assessmentRepository.countByClinicId(clinic.getId()),
                assessmentRepository.findLatestPerformedAt(clinic.getId()).orElse(null),
                recent
        );
    }

    private Clinic getClinic(String email) {
        User user = userRepository.findByEmail(email.toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Authenticated user was not found"));

        if (user.getRole() != User.Role.CLINIC_ADMIN) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Only clinic administrators can manage assessments");
        }

        return clinicRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "No clinic is associated with this account"));
    }
}

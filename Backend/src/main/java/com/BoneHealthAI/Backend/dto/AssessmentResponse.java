package com.BoneHealthAI.Backend.dto;

import com.BoneHealthAI.Backend.entity.Assessment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Getter
public class AssessmentResponse {
    private final Long id;
    private final String assessmentType;
    private final Long patientId;
    private final String patientName;
    private final String resultSummary;
    private final Map<String, Object> resultDetails;
    private final LocalDateTime performedAt;

    public AssessmentResponse(Assessment assessment, ObjectMapper objectMapper) {
        this.id = assessment.getId();
        this.assessmentType = assessment.getAssessmentType().name();
        this.patientId = assessment.getPatient().getId();
        this.patientName = assessment.getPatient().getFullName();
        this.resultSummary = assessment.getResultSummary();
        this.performedAt = assessment.getPerformedAt();

        Map<String, Object> parsed = Collections.emptyMap();
        String json = assessment.getResultDetailsJson();
        if (json != null && !json.isBlank()) {
            try {
                parsed = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            } catch (Exception ignored) {
                // A malformed legacy payload must not break the dashboard response.
            }
        }
        this.resultDetails = parsed;
    }
}

package com.BoneHealthAI.Backend.dto;

import com.BoneHealthAI.Backend.entity.Assessment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class AssessmentCreateRequest {
    private Long patientId;
    private Assessment.Type assessmentType;
    private String resultSummary;
    private Map<String, Object> resultDetails;
}

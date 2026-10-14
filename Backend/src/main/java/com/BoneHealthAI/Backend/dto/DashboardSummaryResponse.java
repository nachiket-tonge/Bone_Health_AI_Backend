package com.BoneHealthAI.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardSummaryResponse {
    private long totalScans;
    private LocalDateTime lastAssessmentAt;
    private List<AssessmentResponse> recentAssessments;
}

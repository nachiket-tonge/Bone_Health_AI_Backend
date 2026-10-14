package com.BoneHealthAI.Backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "assessments",
    indexes = {
        @Index(name = "idx_assessment_clinic_date", columnList = "clinic_id, performed_at"),
        @Index(name = "idx_assessment_patient_date", columnList = "patient_id, performed_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
public class Assessment {

    public enum Type {
        FRACTURE_DETECTION,
        BONE_AGE,
        BMD_RISK
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "assessment_type", nullable = false, length = 40)
    private Type assessmentType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "result_summary", nullable = false, length = 500)
    private String resultSummary;

    // Store structured result values only. Do not store image bytes, base64 images, or PDF data here.
    @Lob
    @Column(name = "result_details_json")
    private String resultDetailsJson;

    @Column(name = "performed_at", nullable = false, updatable = false)
    private LocalDateTime performedAt;

    @PrePersist
    protected void onCreate() {
        performedAt = LocalDateTime.now();
    }
}

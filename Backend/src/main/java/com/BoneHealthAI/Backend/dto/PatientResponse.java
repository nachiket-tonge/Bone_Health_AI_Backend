package com.BoneHealthAI.Backend.dto;

import com.BoneHealthAI.Backend.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PatientResponse {
    private Long id;
    private String fullName;
    private String phone;
    private Integer age;
    private String sex;
    private String address;
    private Long clinicId;
    private LocalDateTime createdAt;

    public static PatientResponse from(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getFullName(),
                patient.getPhone(),
                patient.getAge(),
                patient.getSex(),
                patient.getAddress(),
                patient.getClinic().getId(),
                patient.getCreatedAt()
        );
    }
}

package com.BoneHealthAI.Backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientRequest {
    private String fullName;
    private String phone;
    private Integer age;
    private String sex;
    private String address;
}

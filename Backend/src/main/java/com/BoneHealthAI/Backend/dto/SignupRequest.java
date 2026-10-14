
package com.BoneHealthAI.Backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {

    @NotBlank
    @Size(max = 120)
    private String name;

    @Email
    @NotBlank
    @Size(max = 254)
    private String email;

    @NotBlank
    @Size(min = 8, max = 72)
    private String password;

    @NotBlank
    @Size(max = 200)
    private String clinicName;

    @NotBlank
    @Size(max = 1000)
    private String address;

    @NotBlank
    @Size(max = 30)
    private String phone;
}

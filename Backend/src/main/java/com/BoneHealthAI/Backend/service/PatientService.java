package com.BoneHealthAI.Backend.service;

import com.BoneHealthAI.Backend.dto.PatientRequest;
import com.BoneHealthAI.Backend.dto.PatientResponse;
import com.BoneHealthAI.Backend.entity.Clinic;
import com.BoneHealthAI.Backend.entity.Patient;
import com.BoneHealthAI.Backend.entity.User;
import com.BoneHealthAI.Backend.repository.ClinicRepository;
import com.BoneHealthAI.Backend.repository.PatientRepository;
import com.BoneHealthAI.Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;

    @Transactional(readOnly = true)
    public List<PatientResponse> searchPatients(String authenticatedEmail, String search) {
        Clinic clinic = getClinicForAdministrator(authenticatedEmail);
        List<Patient> patients;

        if (search == null || search.isBlank()) {
            patients = patientRepository.findByClinicIdOrderByFullNameAsc(clinic.getId());
        } else {
            String term = search.trim();
            patients = patientRepository
                    .findByClinicIdAndFullNameContainingIgnoreCaseOrderByFullNameAsc(
                            clinic.getId(), term);
        }

        return patients.stream().map(PatientResponse::from).toList();
    }

    public PatientResponse createPatient(String authenticatedEmail, PatientRequest request) {
        Clinic clinic = getClinicForAdministrator(authenticatedEmail);

        if (request == null || request.getFullName() == null
                || request.getFullName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patient name is required");
        }

        if (request.getAge() != null && (request.getAge() < 0 || request.getAge() > 130)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Age must be between 0 and 130");
        }

        String fullName = request.getFullName().trim().replaceAll("\\s+", " ");
        String phone = clean(request.getPhone());
        String sex = clean(request.getSex());
        String address = clean(request.getAddress());

        if (phone != null && patientRepository.existsByClinicIdAndFullNameIgnoreCaseAndPhone(
                clinic.getId(), fullName, phone)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A patient with this name and phone number already exists in this clinic"
            );
        }

        Patient patient = new Patient();
        patient.setFullName(fullName);
        patient.setPhone(phone);
        patient.setAge(request.getAge());
        patient.setSex(sex);
        patient.setAddress(address);
        patient.setClinic(clinic);

        return PatientResponse.from(patientRepository.save(patient));
    }

    @Transactional(readOnly = true)
    public PatientResponse getPatient(String authenticatedEmail, Long patientId) {
        Clinic clinic = getClinicForAdministrator(authenticatedEmail);
        Patient patient = patientRepository.findByIdAndClinicId(patientId, clinic.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Patient not found"));
        return PatientResponse.from(patient);
    }

    private Clinic getClinicForAdministrator(String email) {
        User user = userRepository.findByEmail(email.toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Authenticated user was not found"));

        if (user.getRole() != User.Role.CLINIC_ADMIN) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Only clinic administrators can manage patients");
        }

        return clinicRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "No clinic is associated with this account"));
    }

    private String clean(String value) {
        if (value == null || value.isBlank()) return null;
        return value.trim();
    }
}

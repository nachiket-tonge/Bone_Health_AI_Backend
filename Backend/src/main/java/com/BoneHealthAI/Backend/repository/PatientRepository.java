package com.BoneHealthAI.Backend.repository;

import com.BoneHealthAI.Backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByClinicIdOrderByFullNameAsc(Long clinicId);

    List<Patient> findByClinicIdAndFullNameContainingIgnoreCaseOrderByFullNameAsc(
            Long clinicId, String fullName);

    Optional<Patient> findByIdAndClinicId(Long id, Long clinicId);

    boolean existsByClinicIdAndFullNameIgnoreCaseAndPhone(
            Long clinicId, String fullName, String phone);
}

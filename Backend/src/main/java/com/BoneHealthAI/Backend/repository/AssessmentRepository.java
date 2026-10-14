package com.BoneHealthAI.Backend.repository;

import com.BoneHealthAI.Backend.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    long countByClinicId(Long clinicId);

    Optional<Assessment> findFirstByClinicIdOrderByPerformedAtDesc(Long clinicId);

    Page<Assessment> findByClinicIdOrderByPerformedAtDesc(Long clinicId, Pageable pageable);

    Page<Assessment> findByClinicIdAndPatientIdOrderByPerformedAtDesc(
            Long clinicId, Long patientId, Pageable pageable);

    @Query("select max(a.performedAt) from Assessment a where a.clinic.id = :clinicId")
    Optional<LocalDateTime> findLatestPerformedAt(@Param("clinicId") Long clinicId);
}

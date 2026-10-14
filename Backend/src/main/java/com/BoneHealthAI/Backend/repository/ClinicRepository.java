
package com.BoneHealthAI.Backend.repository;

import com.BoneHealthAI.Backend.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClinicRepository
        extends JpaRepository<Clinic, Long> {

    Optional<Clinic> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}

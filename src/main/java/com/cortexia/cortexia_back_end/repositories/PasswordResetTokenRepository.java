package com.cortexia.cortexia_back_end.repositories;

import com.cortexia.cortexia_back_end.models.PasswordResetTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenModel, Long> {
    Optional<PasswordResetTokenModel> findByToken(String token);
}
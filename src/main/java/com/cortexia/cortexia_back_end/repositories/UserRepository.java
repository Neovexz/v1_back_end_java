package com.cortexia.cortexia_back_end.repositories;

import com.cortexia.cortexia_back_end.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Integer> {
    Optional<UserModel> findByEmail(String email);
}


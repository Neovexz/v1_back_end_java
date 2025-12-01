package com.cortexia.cortexia_back_end.repositories;

import com.cortexia.cortexia_back_end.models.TecnicoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TecnicoRepository extends JpaRepository<TecnicoModel, Long> {

    Optional<TecnicoModel> findFirstByAtivoTrue();
}

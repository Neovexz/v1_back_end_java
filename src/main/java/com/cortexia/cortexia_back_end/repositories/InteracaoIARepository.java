package com.cortexia.cortexia_back_end.repositories;

import com.cortexia.cortexia_back_end.models.InteracaoIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteracaoIARepository extends JpaRepository<InteracaoIA, Long> {
}

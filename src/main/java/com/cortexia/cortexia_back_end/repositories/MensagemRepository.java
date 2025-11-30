package com.cortexia.cortexia_back_end.repositories;

import com.cortexia.cortexia_back_end.models.MensagemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MensagemRepository extends JpaRepository<MensagemModel, Long> {
    List<MensagemModel> findByChamadoIdOrderByCriadoEmAsc(Long chamadoId);
}

package com.cortexia.cortexia_back_end.repositories;

import com.cortexia.cortexia_back_end.enums.TicketStatus;
import com.cortexia.cortexia_back_end.models.TicketModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<TicketModel, Long> {

    // Busca todos os chamados de um cliente específico
    List<TicketModel> findByClientId(Long clientId);

    // Busca todos os chamados atribuídos a um técnico específico
    List<TicketModel> findByTechnicianId(Long technicianId);

    // Busca todos os chamados por status (OPEN, IN_PROGRESS, CLOSED...)
    List<TicketModel> findByStatus(TicketStatus status);
}

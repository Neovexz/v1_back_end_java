package com.cortexia.support.repository;

import com.cortexia.support.entity.Ticket;
import com.cortexia.support.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByClientId(Long clientId);
    List<Ticket> findByTechnicianId(Long techId);

    // Busca chamados pendentes para técnicos
    List<Ticket> findByStatus(TicketStatus status);
}
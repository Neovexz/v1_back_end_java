package com.cortexia.support.service;

import com.cortexia.support.entity.*;
import com.cortexia.support.repository.TicketRepository;
import com.cortexia.support.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final AIService aiService;

    // --- CONSTRUTOR MANUAL (Sem Lombok) ---
    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, AIService aiService) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.aiService = aiService;
    }

    // CLIENTE abre chamado
    public Ticket createTicket(String username, String title, String description) {
        User client = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // SUBSTITUIÇÃO DO BUILDER PELO JEITO CLÁSSICO
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setClient(client);
        ticket.setStatus(TicketStatus.OPEN);

        // 1. Tentar resolver via IA
        String aiSolution = aiService.attemptResolution(description);

        if (aiSolution != null) {
            ticket.setAiResponse(aiSolution);
            ticket.setFinalSolution(aiSolution);
            ticket.setStatus(TicketStatus.RESOLVED_BY_AI);
        } else {
            ticket.setAiResponse("IA não encontrou solução confiável. Encaminhando para técnico.");
            ticket.setStatus(TicketStatus.PENDING_TECHNICIAN);
        }

        return ticketRepository.save(ticket);
    }

    // TÉCNICO resolve chamado
    public Ticket resolveTicket(Long ticketId, Long techId, String solution) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado"));

        User tech = userRepository.findById(techId)
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado"));

        ticket.setTechnician(tech);
        ticket.setFinalSolution(solution);
        ticket.setStatus(TicketStatus.RESOLVED_BY_TECH);

        // IA aprende
        aiService.learn(ticket.getDescription(), solution);

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getMyTickets(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        if (user.getRole() == Role.TECHNICIAN) {
            return ticketRepository.findByStatus(TicketStatus.PENDING_TECHNICIAN);
        }
        return ticketRepository.findByClientId(user.getId());
    }
}
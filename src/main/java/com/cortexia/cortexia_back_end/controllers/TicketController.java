package com.cortexia.support.controller;

import com.cortexia.support.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    // --- CONSTRUTOR MANUAL (Sem Lombok) ---
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Cliente cria chamado
    @PostMapping
    public ResponseEntity<Ticket> createTicket(
            @RequestBody CreateTicketRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(ticketService.createTicket(
                userDetails.getUsername(),
                request.getTitle(),
                request.getDescription()
        ));
    }

    // Técnico resolve chamado
    @PostMapping("/{id}/resolve")
    public ResponseEntity<Ticket> resolveTicket(
            @PathVariable Long id,
            @RequestBody ResolveRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Em produção, pegaríamos o ID do técnico logado.
        // Aqui vamos fixar 1L ou buscar pelo userDetails se necessário.
        // Por enquanto, vamos passar 1L como ID do técnico apenas para testar.
        return ResponseEntity.ok(ticketService.resolveTicket(id, 1L, request.getSolution()));
    }

    // Listar chamados (Histórico)
    @GetMapping
    public ResponseEntity<List<Ticket>> getMyTickets(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ticketService.getMyTickets(userDetails.getUsername()));
    }
}

// --- CLASSES AUXILIARES COM GETTERS/SETTERS MANUAIS ---

class CreateTicketRequest {
    private String title;
    private String description;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

class ResolveRequest {
    private String solution;

    public String getSolution() { return solution; }
    public void setSolution(String solution) { this.solution = solution; }
}
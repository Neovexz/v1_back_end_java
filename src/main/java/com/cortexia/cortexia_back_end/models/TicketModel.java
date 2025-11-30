package com.cortexia.cortexia_back_end.models;

import com.cortexia.cortexia_back_end.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String aiResponse;

    @Column(columnDefinition = "TEXT")
    private String finalSolution;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private UserModel client;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private UserModel technician;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Criado automaticamente ao salvar
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Atualizado automaticamente ao editar
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

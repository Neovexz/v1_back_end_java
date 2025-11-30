package com.cortexia.cortexia_back_end.models;

import com.cortexia.cortexia_back_end.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "chamados")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChamadoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descricao;

    @Column(length = 120)
    private String local;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)

    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private Impacto impacto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusChamado status;

    @Column(nullable = false)
    private OffsetDateTime criadoEm;

    @Column
    private OffsetDateTime atualizadoEm;
}

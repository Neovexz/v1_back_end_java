package com.cortexia.cortexia_back_end.models;

import com.cortexia.cortexia_back_end.enums.CategoriaChamado;
import com.cortexia.cortexia_back_end.enums.PrioridadeChamado;
import com.cortexia.cortexia_back_end.enums.StatusChamado;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "chamados")
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String local;

    @Enumerated(EnumType.STRING)
    private PrioridadeChamado prioridade;

    @Enumerated(EnumType.STRING)
    private CategoriaChamado categoria;

    private String impacto;

    @Enumerated(EnumType.STRING)
    private StatusChamado status;

    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;

    public Chamado() { }

}

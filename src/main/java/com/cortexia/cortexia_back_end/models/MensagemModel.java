package com.cortexia.cortexia_back_end.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "mensagens")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensagemModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long chamadoId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String conteudo;

    @Column(nullable = false, length = 20)
    private String autor;

    @Column(nullable = false)
    private OffsetDateTime criadoEm;

    @Column(length = 500)
    private String attachmentUrl;
}

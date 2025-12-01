package com.cortexia.cortexia_back_end.models;

import jakarta.persistence.*;
import lombok.*; // Garanta que isso está aqui
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_ia")
@Data
@Builder            // <--- Necessário para o .builder()
@NoArgsConstructor  // <--- Necessário para o Banco de Dados
@AllArgsConstructor // <--- OBRIGATÓRIO para o @Builder funcionar com @NoArgsConstructor
public class InteracaoIA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String perguntaUsuario;

    @Column(columnDefinition = "TEXT")
    private String respostaIA;

    private String nomeUsuario;

    private LocalDateTime dataHora;

    @PrePersist
    protected void onCreate() {
        this.dataHora = LocalDateTime.now();
    }
}

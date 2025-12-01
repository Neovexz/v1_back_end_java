package com.cortexia.cortexia_back_end.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tecnicos")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TecnicoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private boolean ativo;
}

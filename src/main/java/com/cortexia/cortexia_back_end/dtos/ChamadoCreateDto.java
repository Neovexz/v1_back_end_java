package com.cortexia.cortexia_back_end.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChamadoCreateDto(

        @NotBlank(message = "Título é obrigatório")
        @Size(min = 3, max = 150, message = "Título deve ter entre 3 e 150 caracteres")
        String titulo,

        @NotBlank(message = "Descrição é obrigatória")
        @Size(min = 5, max = 4000, message = "Descrição inválida (mín. 5 caracteres)")
        String descricao,

        @Size(max = 120, message = "Local não pode exceder 120 caracteres")
        String local,

        @NotBlank(message = "Prioridade é obrigatória")
        String prioridade,

        @NotBlank(message = "Impacto é obrigatório")
        String impacto,

        @NotBlank(message = "Categoria é obrigatória")
        String categoria
) {}

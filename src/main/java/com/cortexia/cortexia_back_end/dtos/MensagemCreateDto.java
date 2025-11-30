package com.cortexia.cortexia_back_end.dtos;

import jakarta.validation.constraints.*;

public record MensagemCreateDto(
        @NotBlank(message = "Conteúdo é obrigatório")
        @Size(min = 1, max = 4000) String conteudo,
        @NotBlank(message = "Autor é obrigatório") String autor
) {}

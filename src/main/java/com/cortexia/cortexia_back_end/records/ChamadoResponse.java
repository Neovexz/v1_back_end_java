package com.cortexia.cortexia_back_end.records;

import com.cortexia.cortexia_back_end.enums.*;
import java.time.OffsetDateTime;

public record ChamadoResponse(
        Long id,
        String titulo,
        String descricao,
        String local,
        Prioridade prioridade,
        Impacto impacto,
        Categoria categoria,
        StatusChamado status,
        OffsetDateTime criadoEm,
        OffsetDateTime atualizadoEm
) {}

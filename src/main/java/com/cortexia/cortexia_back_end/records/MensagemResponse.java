package com.cortexia.cortexia_back_end.records;

import java.time.OffsetDateTime;

public record MensagemResponse(
        Long id,
        Long chamadoId,
        String conteudo,
        String autor,
        OffsetDateTime criadoEm,
        String attachmentUrl
) {}

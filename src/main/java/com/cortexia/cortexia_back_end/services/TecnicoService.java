package com.cortexia.cortexia_back_end.services;

import com.cortexia.cortexia_back_end.models.MensagemModel;
import org.springframework.stereotype.Service;

@Service
public class TecnicoService {
    // Mock: em produção integra com sistema de notificação / ticketing
    public void notifyEscalation(Long chamadoId, MensagemModel userMessage) {
        // log or webhook (placeholder)
        System.out.println("ESCALADA: chamado=" + chamadoId + " mensagemId=" + userMessage.getId());
    }
}

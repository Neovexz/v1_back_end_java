package com.cortexia.cortexia_back_end.services;

import com.cortexia.cortexia_back_end.models.MensagemModel;
import com.cortexia.cortexia_back_end.models.TecnicoModel;
import com.cortexia.cortexia_back_end.repositories.TecnicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;

    /**
     * Retorna um técnico disponível (mock simples).
     */
    public Optional<TecnicoModel> getAvailableTechnician() {
        return tecnicoRepository.findFirstByAtivoTrue();
    }

    /**
     * Notifica técnico sobre a escalada do chamado.
     */
    public void notifyEscalation(Long chamadoId, MensagemModel userMessage) {
        System.out.println("""
                🚨 ESCALADA DE CHAMADO
                → Chamado: %d
                → MensagemId: %d
                → Conteúdo: %s
                """.formatted(
                chamadoId,
                userMessage.getId(),
                userMessage.getConteudo()
        ));
    }
}

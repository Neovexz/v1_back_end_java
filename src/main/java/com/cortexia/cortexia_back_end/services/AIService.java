package com.cortexia.cortexia_back_end.services;

import com.cortexia.cortexia_back_end.models.MensagemModel;
import com.cortexia.cortexia_back_end.repositories.MensagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AIService {

    private final MensagemRepository mensagemRepository;
    private final TecnicoService technicianService;

    // MOCK: tenta encontrar resposta na "knowledge base"
    private Optional<String> searchKnowledgeBase(String texto) {
        // Exemplo simples: if contains 'senha' devolve resposta, etc.
        String lower = texto.toLowerCase();
        if (lower.contains("senha")) {
            return Optional.of("Verifique a política de senha: redefina via portal em Portal > Redefinir senha.");
        }
        // não encontrou
        return Optional.empty();
    }

    @Transactional
    public void processUserMessage(Long chamadoId, MensagemModel userMessage) {
        var maybe = searchKnowledgeBase(userMessage.getConteudo());
        if (maybe.isPresent()) {
            MensagemModel ai = MensagemModel.builder()
                    .chamadoId(chamadoId)
                    .conteudo(maybe.get())
                    .autor("AI")
                    .criadoEm(OffsetDateTime.now())
                    .build();
            mensagemRepository.save(ai);
        } else {
            MensagemModel system = MensagemModel.builder()
                    .chamadoId(chamadoId)
                    .conteudo("A IA não encontrou uma resposta adequada. Encaminhando para um técnico. ✅")
                    .autor("SYSTEM")
                    .criadoEm(OffsetDateTime.now())
                    .build();
            mensagemRepository.save(system);
            technicianService.notifyEscalation(chamadoId, userMessage);
        }
    }
}

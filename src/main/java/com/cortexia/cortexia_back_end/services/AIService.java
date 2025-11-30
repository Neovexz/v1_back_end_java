package com.cortexia.support.service;

import com.cortexia.support.repository.KnowledgeBaseRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AIService {

    private final KnowledgeBaseRepository knowledgeRepository;

    // --- CONSTRUTOR MANUAL (Sem Lombok) ---
    public AIService(KnowledgeBaseRepository knowledgeRepository) {
        this.knowledgeRepository = knowledgeRepository;
    }

    // 1. Tenta resolver buscando no banco de conhecimento
    public String attemptResolution(String description) {
        List<KnowledgeBase> knowledge = knowledgeRepository.findSimilarProblems(description);

        if (!knowledge.isEmpty()) {
            return knowledge.get(0).getSolution(); // Agora funciona pois criamos o Getter
        }
        return null;
    }

    // 2. Aprende com a solução do técnico
    public void learn(String problem, String solution) {
        // --- CRIAÇÃO MANUAL (Sem Builder) ---
        KnowledgeBase newKnowledge = new KnowledgeBase();
        newKnowledge.setProblemPattern(problem);
        newKnowledge.setSolution(solution);

        knowledgeRepository.save(newKnowledge);
    }
}
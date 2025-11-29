package com.cortexia.support.repository;

import com.cortexia.support.entity.KnowledgeBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface KnowledgeBaseRepository extends JpaRepository<KnowledgeBase, Long> {
    // Busca simples simulando IA (procura palavras chave)
    @Query("SELECT k FROM KnowledgeBase k WHERE k.problemPattern LIKE %:keyword%")
    List<KnowledgeBase> findSimilarProblems(String keyword);
}

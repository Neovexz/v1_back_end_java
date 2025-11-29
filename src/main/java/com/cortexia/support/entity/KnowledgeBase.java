package com.cortexia.support.entity;

import jakarta.persistence.*;

@Entity
public class KnowledgeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String problemPattern;

    @Column(columnDefinition = "TEXT")
    private String solution;

    // --- CONSTRUTOR VAZIO ---
    public KnowledgeBase() {}

    // --- GETTERS E SETTERS MANUAIS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProblemPattern() { return problemPattern; }
    public void setProblemPattern(String problemPattern) { this.problemPattern = problemPattern; }

    public String getSolution() { return solution; }
    public void setSolution(String solution) { this.solution = solution; }
}
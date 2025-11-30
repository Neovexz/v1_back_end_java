package com.cortexia.cortexia_back_end.dtos;

import java.util.Map;

public class DashboardData {
    // Campos da tela de IA
    private String padroesIdentificados;
    private String sugestaoMelhoria;
    private String alertaTendencia;

    // Status das automações (Ativo, Em Teste, etc)
    private Map<String, String> automacoes;

    // Construtor
    public DashboardData(String padroes, String sugestao, String alerta, Map<String, String> automacoes) {
        this.padroesIdentificados = padroes;
        this.sugestaoMelhoria = sugestao;
        this.alertaTendencia = alerta;
        this.automacoes = automacoes;
    }

    // Getters e Setters
    public String getPadroesIdentificados() { return padroesIdentificados; }
    public void setPadroesIdentificados(String padroesIdentificados) { this.padroesIdentificados = padroesIdentificados; }

    public String getSugestaoMelhoria() { return sugestaoMelhoria; }
    public void setSugestaoMelhoria(String sugestaoMelhoria) { this.sugestaoMelhoria = sugestaoMelhoria; }

    public String getAlertaTendencia() { return alertaTendencia; }
    public void setAlertaTendencia(String alertaTendencia) { this.alertaTendencia = alertaTendencia; }

    public Map<String, String> getAutomacoes() { return automacoes; }
    public void setAutomacoes(Map<String, String> automacoes) { this.automacoes = automacoes; }
}
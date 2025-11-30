package com.cortexia.cortexia_back_end.controllers;

import com.cortexia.cortexia_back_end.dtos.DashboardData;
import com.cortexia.cortexia_back_end.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DashboardController {

    private final TicketRepository ticketRepository;

    @GetMapping("/analytics")
    public ResponseEntity<DashboardData> getIAAnalytics() {

        long totalChamados = ticketRepository.count();

        String padrao;
        String tendencia;

        if (totalChamados > 10) {
            padrao = "Alto volume de chamados sobre impressoras detectado.";
            tendencia = "Aumento de 40% em problemas de hardware nesta semana.";
        } else {
            padrao = "O sistema está estável com poucos incidentes.";
            tendencia = "Fluxo de chamados dentro da normalidade.";
        }

        String sugestao =
                "Criar um tutorial automático para reinicialização de equipamentos.";

        Map<String, String> statusAutoma = new HashMap<>();
        statusAutoma.put("classificacao", "Ativo");
        statusAutoma.put("sugestao", "Ativo");
        statusAutoma.put("priorizacao", "Ativo");
        statusAutoma.put("sentimento", "Em Teste");

        DashboardData dados = new DashboardData(
                padrao,
                sugestao,
                tendencia,
                statusAutoma
        );

        return ResponseEntity.ok(dados);
    }
}

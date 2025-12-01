package com.cortexia.cortexia_back_end.controllers;

import com.cortexia.cortexia_back_end.models.TecnicoModel;
import com.cortexia.cortexia_back_end.repositories.TecnicoRepository;
import com.cortexia.cortexia_back_end.services.ChamadoService;
import com.cortexia.cortexia_back_end.services.MensagemService;
import com.cortexia.cortexia_back_end.services.TecnicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/chamados/{chamadoId}")
@RequiredArgsConstructor
public class AtribuicaoController {

    private final TecnicoService tecnicoService;
    private final ChamadoService chamadoService;
    private final MensagemService mensagemService;
    private final TecnicoRepository tecnicoRepository;

    @PostMapping("/atribuir-tecnico")
    public ResponseEntity<?> atribuirTecnico(
            @PathVariable Long chamadoId,
            @RequestParam(value = "tecnicoId", required = false) Long tecnicoIdParam
    ) {
        Long tecnicoId = tecnicoIdParam;

        if (tecnicoId == null) {
            Optional<TecnicoModel> t = tecnicoService.getAvailableTechnician();
            if (t.isEmpty()) {
                return ResponseEntity.badRequest().body("Nenhum técnico disponível");
            }
            tecnicoId = t.get().getId();
        } else {
            if (!tecnicoRepository.existsById(tecnicoId)) {
                return ResponseEntity.badRequest().body("Técnico não encontrado: " + tecnicoId);
            }
        }

        chamadoService.atribuirTecnico(chamadoId, tecnicoId);

        String nomeTec = tecnicoRepository.findById(tecnicoId).map(TecnicoModel::getNome).orElse("Técnico");
        String texto = "Encaminhei seu chamado para o técnico " + nomeTec + " (id: " + tecnicoId + "). Ele vai entrar em contato por aqui.";

        var resp = mensagemService.criar(chamadoId, new com.cortexia.cortexia_back_end.dtos.MensagemCreateDto(texto, "IA"));

        tecnicoService.notifyEscalation(chamadoId, null);

        return ResponseEntity.ok(resp);
    }
}


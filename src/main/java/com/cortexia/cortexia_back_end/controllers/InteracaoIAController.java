package com.cortexia.cortexia_back_end.controllers;

import com.cortexia.cortexia_back_end.models.InteracaoIA;
import com.cortexia.cortexia_back_end.repositories.InteracaoIARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/historico-ia")
@CrossOrigin(origins = "*")
public class InteracaoIAController {

    @Autowired
    private InteracaoIARepository repository;

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarConversa(@RequestBody Map<String, String> dados) {

        // Usando o BUILDER do Lombok (Mais chique e organizado)
        InteracaoIA novaInteracao = InteracaoIA.builder()
                .perguntaUsuario(dados.get("pergunta"))
                .respostaIA(dados.get("resposta"))
                .nomeUsuario(dados.get("usuario"))
                .build();

        repository.save(novaInteracao);

        return ResponseEntity.ok("Histórico salvo com sucesso!");
    }
}

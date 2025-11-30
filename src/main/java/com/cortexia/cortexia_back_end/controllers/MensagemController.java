package com.cortexia.cortexia_back_end.controllers;

import com.cortexia.cortexia_back_end.dtos.MensagemCreateDto;
import com.cortexia.cortexia_back_end.records.MensagemResponse;
import com.cortexia.cortexia_back_end.services.MensagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chamados/{chamadoId}/mensagens")
@RequiredArgsConstructor
public class MensagemController {

    private final MensagemService service;

    @GetMapping
    public ResponseEntity<List<MensagemResponse>> listar(@PathVariable Long chamadoId) {
        return ResponseEntity.ok(service.listarPorChamado(chamadoId));
    }

    @PostMapping
    public ResponseEntity<MensagemResponse> criar(@PathVariable Long chamadoId, @Valid @RequestBody MensagemCreateDto dto) {
        var resp = service.criar(chamadoId, dto);
        return ResponseEntity.status(201).body(resp);
    }

    @PostMapping("/imagem")
    public ResponseEntity<MensagemResponse> uploadImagem(@PathVariable Long chamadoId,
                                                         @RequestParam("file") MultipartFile file,
                                                         @RequestParam("autor") String autor) {
        var resp = service.criarComImagem(chamadoId, file, autor);
        return ResponseEntity.status(201).body(resp);
    }
}

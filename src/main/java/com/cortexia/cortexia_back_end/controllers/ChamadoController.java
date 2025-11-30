package com.cortexia.cortexia_back_end.controllers;

import com.cortexia.cortexia_back_end.dtos.ChamadoCreateDto;
import com.cortexia.cortexia_back_end.enums.StatusChamado;
import com.cortexia.cortexia_back_end.records.ChamadoResponse;
import com.cortexia.cortexia_back_end.services.ChamadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chamados")
@RequiredArgsConstructor
public class ChamadoController {

    private final ChamadoService service;

    @PostMapping
    public ResponseEntity<ChamadoResponse> criar(@Valid @RequestBody ChamadoCreateDto dto) {
        ChamadoResponse resp = service.criar(dto);
        return ResponseEntity.status(201).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChamadoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @GetMapping
    public ResponseEntity<Page<ChamadoResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ChamadoResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusChamado status
    ) {
        return ResponseEntity.ok(service.atualizarStatus(id, status));
    }
}

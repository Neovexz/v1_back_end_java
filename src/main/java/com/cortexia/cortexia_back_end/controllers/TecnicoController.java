package com.cortexia.cortexia_back_end.controllers;

import com.cortexia.cortexia_back_end.models.TecnicoModel;
import com.cortexia.cortexia_back_end.repositories.TecnicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tecnicos")
@RequiredArgsConstructor
public class TecnicoController {

    private final TecnicoRepository tecnicoRepository;

    @GetMapping
    public ResponseEntity<List<TecnicoModel>> listar() {
        return ResponseEntity.ok(tecnicoRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<TecnicoModel> criar(@RequestBody TecnicoModel t) {
        var saved = tecnicoRepository.save(t);
        return ResponseEntity.status(201).body(saved);
    }
}

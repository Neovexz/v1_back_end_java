package com.cortexia.cortexia_back_end.services;

import com.cortexia.cortexia_back_end.dtos.MensagemCreateDto;
import com.cortexia.cortexia_back_end.models.MensagemModel;
import com.cortexia.cortexia_back_end.records.MensagemResponse;
import com.cortexia.cortexia_back_end.repositories.MensagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MensagemService {

    private final MensagemRepository repository;
    private final AIService aiService;

    @Transactional(readOnly = true)
    public List<MensagemResponse> listarPorChamado(Long chamadoId) {
        return repository.findByChamadoIdOrderByCriadoEmAsc(chamadoId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MensagemResponse criar(Long chamadoId, MensagemCreateDto dto) {
        MensagemModel m = MensagemModel.builder()
                .chamadoId(chamadoId)
                .conteudo(dto.conteudo())
                .autor(dto.autor())
                .criadoEm(OffsetDateTime.now())
                .build();

        var saved = repository.save(m);

        // se for usuário, tenta processar com IA
        if ("USER".equalsIgnoreCase(saved.getAutor())) {
            aiService.processUserMessage(chamadoId, saved);
        }

        return toResponse(saved);
    }

    @Transactional
    public MensagemResponse criarComImagem(Long chamadoId, org.springframework.web.multipart.MultipartFile file, String autor) {
        // Salva localmente em /uploads (placeholder)
        try {
            var filename = java.util.UUID.randomUUID() + "_" + file.getOriginalFilename();
            java.nio.file.Path storage = java.nio.file.Paths.get("uploads").resolve(filename);
            java.nio.file.Files.createDirectories(storage.getParent());
            file.transferTo(storage.toFile());
            String url = "/uploads/" + filename; // configure server para servir essa pasta em produção
            MensagemModel m = MensagemModel.builder()
                    .chamadoId(chamadoId)
                    .conteudo("[Imagem enviada]")
                    .autor(autor)
                    .attachmentUrl(url)
                    .criadoEm(OffsetDateTime.now())
                    .build();
            var saved = repository.save(m);
            if ("USER".equalsIgnoreCase(autor)) {
                aiService.processUserMessage(chamadoId, saved);
            }
            return toResponse(saved);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar imagem", e);
        }
    }

    private MensagemResponse toResponse(MensagemModel m) {
        return new MensagemResponse(m.getId(), m.getChamadoId(), m.getConteudo(), m.getAutor(), m.getCriadoEm(), m.getAttachmentUrl());
    }
}

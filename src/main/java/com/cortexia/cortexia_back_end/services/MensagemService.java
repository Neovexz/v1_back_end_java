package com.cortexia.cortexia_back_end.services;

import com.cortexia.cortexia_back_end.dtos.MensagemCreateDto;
import com.cortexia.cortexia_back_end.models.MensagemModel;
import com.cortexia.cortexia_back_end.records.MensagemResponse;
import com.cortexia.cortexia_back_end.repositories.MensagemRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MensagemService {

    private final MensagemRepository repository;
    private final AIService aiService;

    // ------------------------------------------------------------------------
    // LISTAR MENSAGENS DO CHAMADO
    // ------------------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<MensagemResponse> listarPorChamado(Long chamadoId) {
        return repository.findByChamadoIdOrderByCriadoEmAsc(chamadoId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ------------------------------------------------------------------------
    // CRIAR MENSAGEM DE TEXTO
    // ------------------------------------------------------------------------
    @Transactional
    public MensagemResponse criar(Long chamadoId, MensagemCreateDto dto) {

        MensagemModel m = MensagemModel.builder()
                .chamadoId(chamadoId)
                .conteudo(dto.conteudo())
                .autor(dto.autor())
                .criadoEm(OffsetDateTime.now())
                .build();

        var saved = repository.save(m);

        // 🚨 Somente usuário ativa IA (IA e SYSTEM não ativam)
        if ("USER".equalsIgnoreCase(saved.getAutor())) {
            try {
                aiService.processUserMessage(chamadoId, saved);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return toResponse(saved);
    }

    // ------------------------------------------------------------------------
    // CRIAR MENSAGEM COM IMAGEM
    // ------------------------------------------------------------------------
    @Transactional
    public MensagemResponse criarComImagem(Long chamadoId, MultipartFile file, String autor) {
        try {
            var filename = java.util.UUID.randomUUID() + "_" + file.getOriginalFilename();
            var storage = java.nio.file.Paths.get("uploads").resolve(filename);
            java.nio.file.Files.createDirectories(storage.getParent());

            file.transferTo(storage.toFile());

            String url = "/uploads/" + filename;

            MensagemModel m = MensagemModel.builder()
                    .chamadoId(chamadoId)
                    .conteudo("[Imagem enviada]")
                    .autor(autor)
                    .attachmentUrl(url)
                    .criadoEm(OffsetDateTime.now())
                    .build();

            var saved = repository.save(m);

            // 🚨 novamente, só chamada da IA quando for o USER
            if ("USER".equalsIgnoreCase(autor)) {
                try {
                    aiService.processUserMessage(chamadoId, saved);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            return toResponse(saved);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar imagem", e);
        }
    }

    // ------------------------------------------------------------------------
    // MAPEAR ENTITY → RESPONSE
    // ------------------------------------------------------------------------
    private MensagemResponse toResponse(MensagemModel m) {
        return new MensagemResponse(
                m.getId(),
                m.getChamadoId(),
                m.getConteudo(),
                m.getAutor(),
                m.getCriadoEm(),
                m.getAttachmentUrl()
        );
    }
}

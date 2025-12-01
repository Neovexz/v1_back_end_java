package com.cortexia.cortexia_back_end.services;

import com.cortexia.cortexia_back_end.dtos.ChamadoCreateDto;
import com.cortexia.cortexia_back_end.dtos.MensagemCreateDto;
import com.cortexia.cortexia_back_end.enums.*;
import com.cortexia.cortexia_back_end.exceptions.NotFoundException;
import com.cortexia.cortexia_back_end.exceptions.BadRequestException;
import com.cortexia.cortexia_back_end.models.ChamadoModel;
import com.cortexia.cortexia_back_end.records.ChamadoResponse;
import com.cortexia.cortexia_back_end.repositories.ChamadoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ChamadoService {

    private final ChamadoRepository repository;
    private final MensagemService mensagemService;

    // =======================================================
    // CRIAR CHAMADO
    // =======================================================
    @Transactional
    public ChamadoResponse criar(ChamadoCreateDto dto) {

        ChamadoModel chamado = ChamadoModel.builder()
                .titulo(dto.titulo())
                .descricao(dto.descricao())
                .local(dto.local())
                .prioridade(convert(Prioridade.class, dto.prioridade(), "prioridade"))
                .impacto(convert(Impacto.class, dto.impacto(), "impacto"))
                .categoria(convert(Categoria.class, dto.categoria(), "categoria"))
                .status(StatusChamado.ABERTO)
                .criadoEm(OffsetDateTime.now())
                .atualizadoEm(OffsetDateTime.now())
                .build();

        repository.saveAndFlush(chamado);

        // ------------------------------------------------------------
        // MENSAGEM INICIAL LIMPA E FORMATADA
        // ------------------------------------------------------------
        String inicial = """
✨ Chamado criado com sucesso!

Aqui estão os detalhes registrados:

📌 Informações
• Título: %s
• Prioridade: %s
• Impacto: %s
• Categoria: %s
• Local: %s

📝 Descrição
%s

Se precisar, envie mensagens, imagens ou mais detalhes por aqui.
""".formatted(
                chamado.getTitulo(),
                prioridadeToLabel(chamado.getPrioridade()),
                impactoToLabel(chamado.getImpacto()),
                categoriaToLabel(chamado.getCategoria()),
                chamado.getLocal(),
                chamado.getDescricao()
        );


        mensagemService.criar(
                chamado.getId(),
                new MensagemCreateDto(inicial, "SYSTEM")
        );

        return toResponse(chamado);
    }

    // =======================================================
    // ENUMS → TEXTO LEGÍVEL
    // =======================================================

    private String prioridadeToLabel(Prioridade p) {
        return switch (p) {
            case BAIXA -> "Baixa";
            case MEDIA -> "Média";
            case ALTA -> "Alta";
            case CRITICA -> "Crítica";
        };
    }

    private String impactoToLabel(Impacto i) {
        return switch (i) {
            case UMA_PESSOA -> "Individual";
            case UM_SETOR -> "Um setor";
            case EMPRESA_INTEIRA -> "Toda a empresa";
            default -> "Desconhecido";
        };
    }

    private String categoriaToLabel(Categoria c) {
        return switch (c) {
            case SOFTWARE -> "Sistema";
            case HARDWARE -> "Computador";
            case REDE -> "Rede";
            case OUTROS -> "Outros";
            default -> "Desconhecido";
        };
    }

    // =======================================================
    // CONSULTAS
    // =======================================================

    @Transactional(readOnly = true)
    public ChamadoResponse buscar(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Chamado não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public Page<ChamadoResponse> listar(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    // =======================================================
    // STATUS
    // =======================================================

    @Transactional
    public ChamadoResponse atualizarStatus(Long id, StatusChamado status) {

        ChamadoModel chamado = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chamado não encontrado: " + id));

        chamado.setStatus(status);
        chamado.setAtualizadoEm(OffsetDateTime.now());
        repository.save(chamado);

        return toResponse(chamado);
    }

    // =======================================================
    // ATRIBUIR TÉCNICO (SEM MEXER NO STATUS)
    // =======================================================

    @Transactional
    public void atribuirTecnico(Long chamadoId, Long tecnicoId) {
        ChamadoModel chamado = repository.findById(chamadoId)
                .orElseThrow(() -> new NotFoundException("Chamado não encontrado: " + chamadoId));

        chamado.setTecnicoId(tecnicoId);
        chamado.setAtualizadoEm(OffsetDateTime.now());
        repository.save(chamado);
    }

    // =======================================================
    // HELPERS
    // =======================================================

    private <T extends Enum<T>> T convert(Class<T> enumClass, String value, String campo) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException("O campo '" + campo + "' é obrigatório.");
        }
        try {
            return Enum.valueOf(enumClass, value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(
                    "Valor inválido para '" + campo + "'. Valor: '" + value +
                            "'. Valores permitidos: " + java.util.Arrays.toString(enumClass.getEnumConstants())
            );
        }
    }

    private ChamadoResponse toResponse(ChamadoModel c) {
        return new ChamadoResponse(
                c.getId(),
                c.getTitulo(),
                c.getDescricao(),
                c.getLocal(),
                c.getPrioridade(),
                c.getImpacto(),
                c.getCategoria(),
                c.getStatus(),
                c.getCriadoEm(),
                c.getAtualizadoEm()
        );
    }
}

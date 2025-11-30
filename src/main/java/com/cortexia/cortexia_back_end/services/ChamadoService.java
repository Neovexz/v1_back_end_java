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

        // Salva garantindo ID já disponível
        repository.saveAndFlush(chamado);

        // ---------- MENSAGEM INICIAL DO CHAT ----------
        String inicial = """
✨ **Chamado criado com sucesso!**

Aqui estão os detalhes registrados:

📌 **Informações**
• **Título:** %s
• **Prioridade:** %s
• **Impacto:** %s
• **Categoria:** %s
• **Local:** %s

📝 **Descrição fornecida**
%s

Se precisar, envie mensagens, imagens ou mais detalhes por aqui.
""".formatted(
                chamado.getTitulo(),
                chamado.getPrioridade(),
                chamado.getImpacto(),
                chamado.getCategoria(),
                chamado.getLocal(),
                chamado.getDescricao()
        );

        // cria primeira mensagem automática
        mensagemService.criar(
                chamado.getId(),
                new MensagemCreateDto(inicial, "SYSTEM")
        );

        return toResponse(chamado);
    }

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

    @Transactional
    public ChamadoResponse atualizarStatus(Long id, StatusChamado status) {

        ChamadoModel chamado = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chamado não encontrado: " + id));

        chamado.setStatus(status);
        chamado.setAtualizadoEm(OffsetDateTime.now());

        repository.save(chamado);

        return toResponse(chamado);
    }

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

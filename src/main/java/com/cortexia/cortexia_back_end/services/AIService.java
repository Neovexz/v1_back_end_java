package com.cortexia.cortexia_back_end.services;

import com.cortexia.cortexia_back_end.models.ChamadoModel;
import com.cortexia.cortexia_back_end.models.MensagemModel;
import com.cortexia.cortexia_back_end.repositories.ChamadoRepository;
import com.cortexia.cortexia_back_end.repositories.MensagemRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AIService {

    private final MensagemRepository mensagemRepository;
    private final TecnicoService tecnicoService;
    private final ChamadoRepository chamadoRepository;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate rest = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    // =============================================================
    // 1) BASE DE CONHECIMENTO
    // =============================================================
    private Optional<String> searchKnowledgeBase(String texto) {
        String lower = texto.toLowerCase();

        if (lower.contains("senha") || lower.contains("login")) {
            return Optional.of("""
🔐 *Acesso e Senha*

Use a opção “Esqueci minha senha”.
Se quiser, posso te ajudar agora mesmo!
""");
        }

        if (lower.contains("lento") || lower.contains("travando")) {
            return Optional.of("""
⚙️ *Computador lento*

Feche programas em segundo plano e tente reiniciar.
Se continuar, posso chamar um técnico!
""");
        }

        return Optional.empty();
    }

    // =============================================================
    // 2) GEMINI VIA REST (FUNCIONA SEM DEPENDÊNCIA)
    // =============================================================
    private String gerarRespostaGemini(String pergunta) {
        try {
            String url =
                    "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key="
                            + geminiApiKey;

            String json = """
            {
              "contents": [{
                "role": "user",
                "parts": [{ "text": "%s" }]
              }]
            }
            """.formatted(pergunta.replace("\"", "'"));

            HttpHeaders h = new HttpHeaders();
            h.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(json, h);

            ResponseEntity<String> resp = rest.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            JsonNode root = mapper.readTree(resp.getBody());

            return root
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {
            return "Não consegui resolver, vou encaminhar para um técnico humano.";
        }
    }

    // =============================================================
    // 3) PROCESSAR MENSAGEM DO USUÁRIO
    // =============================================================
    @Transactional
    public void processUserMessage(Long chamadoId, MensagemModel userMessage) {

        String texto = userMessage.getConteudo();

        // 1) Tentar base de conhecimento
        Optional<String> bk = searchKnowledgeBase(texto);
        if (bk.isPresent()) {
            salvarRespostaIA(chamadoId, bk.get());
            return;
        }

        // 2) Tentar Gemini
        String respostaGemini = gerarRespostaGemini(texto);
        salvarRespostaIA(chamadoId, respostaGemini);

        // 3) Se IA admite que não consegue → Escala para técnico
        if (respostaGemini.toLowerCase().contains("técnico humano")) {
            encaminharParaTecnico(chamadoId, userMessage);
        }
    }

    // =============================================================
    // 4) SALVAR RESPOSTA DA IA
    // =============================================================
    private void salvarRespostaIA(Long chamadoId, String conteudo) {

        MensagemModel ia = MensagemModel.builder()
                .chamadoId(chamadoId)
                .conteudo(conteudo)
                .autor("IA")
                .criadoEm(OffsetDateTime.now())
                .build();

        mensagemRepository.save(ia);
    }

    // =============================================================
    // 5) ENCAMINHAR PARA TÉCNICO
    // =============================================================
    private void encaminharParaTecnico(Long chamadoId, MensagemModel userMessage) {

        var tecnicoOpt = tecnicoService.getAvailableTechnician();

        if (tecnicoOpt.isPresent()) {
            var tecnico = tecnicoOpt.get();

            ChamadoModel chamado = chamadoRepository.findById(chamadoId)
                    .orElseThrow();

            chamado.setTecnicoId(tecnico.getId());
            chamado.setAtualizadoEm(OffsetDateTime.now());
            chamadoRepository.save(chamado);

            salvarRespostaIA(
                    chamadoId,
                    "Encaminhei para o técnico **" + tecnico.getNome() + "** 👨‍🔧"
            );

            tecnicoService.notifyEscalation(chamadoId, userMessage);

        } else {
            salvarRespostaIA(
                    chamadoId,
                    "Nenhum técnico disponível no momento. Seu chamado está na fila. ⏳"
            );
        }
    }
}

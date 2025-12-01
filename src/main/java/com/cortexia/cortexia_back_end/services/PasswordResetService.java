package com.cortexia.cortexia_back_end.services;

import com.cortexia.cortexia_back_end.models.PasswordResetTokenModel;
import com.cortexia.cortexia_back_end.models.UserModel;
import com.cortexia.cortexia_back_end.repositories.PasswordResetTokenRepository;
import com.cortexia.cortexia_back_end.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // ======================================================
    // 1. SOLICITAR TROCA DE SENHA
    // ======================================================
    public void solicitarReset(String email) {

        Optional<UserModel> userOpt = userRepository.findByEmail(email);

        // 🔒 Não revelar se o email existe
        if (userOpt.isEmpty()) {
            return;
        }

        String token = UUID.randomUUID().toString();

        PasswordResetTokenModel reset = PasswordResetTokenModel.builder()
                .email(email)
                .token(token)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();

        tokenRepository.save(reset);

        String link = "http://localhost:8080/auth/reset-password?token=" + token;

        emailService.enviarEmail(
                email,
                "Redefinição de Senha - CortexIA",
                "Olá!\n\nClique no link abaixo para redefinir sua senha:\n\n" + link +
                        "\n\nEste link expira em 15 minutos."
        );
    }

    // ======================================================
    // 2. REDEFINIR SENHA
    // ======================================================
    public boolean redefinirSenha(String token, String novaSenha) {

        PasswordResetTokenModel reset = tokenRepository.findByToken(token)
                .orElse(null);

        if (reset == null) return false;
        if (reset.getExpiresAt().isBefore(LocalDateTime.now())) return false;

        UserModel user = userRepository.findByEmail(reset.getEmail()).orElse(null);
        if (user == null) return false;

        user.setPassword(passwordEncoder.encode(novaSenha));
        userRepository.save(user);

        // 🔐 Apagar token após uso
        tokenRepository.delete(reset);

        return true;
    }
}

package com.cortexia.cortexia_back_end.controllers;

import com.cortexia.cortexia_back_end.services.PasswordResetService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/password")
@RequiredArgsConstructor
@CrossOrigin
public class PasswordResetController {

    private final PasswordResetService resetService;

    @PostMapping("/forgot")
    public ResponseEntity<String> forgot(@RequestBody ForgotDTO dto) {
        resetService.solicitarReset(dto.email);
        return ResponseEntity.ok("Se existir, enviaremos um email com instruções.");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> reset(@RequestBody ResetDTO dto) {

        boolean ok = resetService.redefinirSenha(dto.token, dto.newPassword);

        if (ok) return ResponseEntity.ok("Senha redefinida com sucesso!");
        return ResponseEntity.badRequest().body("Token inválido ou expirado.");
    }
}

@Data class ForgotDTO {
    public String email;
}

@Data class ResetDTO {
    public String token;
    public String newPassword;
}

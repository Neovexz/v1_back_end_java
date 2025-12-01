package com.cortexia.cortexia_back_end.controllers;

import com.cortexia.cortexia_back_end.dtos.AuthRequest;
import com.cortexia.cortexia_back_end.dtos.AuthResponse;
import com.cortexia.cortexia_back_end.dtos.RegisterRequest;
import com.cortexia.cortexia_back_end.infra.security.JwtService;
import com.cortexia.cortexia_back_end.models.UserModel;
import com.cortexia.cortexia_back_end.repositories.UserRepository;
import com.cortexia.cortexia_back_end.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
}

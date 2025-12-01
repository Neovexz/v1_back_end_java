package com.cortexia.cortexia_back_end.services;

import com.cortexia.cortexia_back_end.dtos.AuthRequest;
import com.cortexia.cortexia_back_end.dtos.AuthResponse;
import com.cortexia.cortexia_back_end.dtos.RegisterRequest;
import com.cortexia.cortexia_back_end.infra.security.JwtService;
import com.cortexia.cortexia_back_end.models.UserModel;
import com.cortexia.cortexia_back_end.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // REGISTRO
    public String register(RegisterRequest req) {

        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email já está em uso!");
        }

        UserModel user = UserModel.builder()
                .nome(req.getNome())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .build();

        userRepository.save(user);
        return "Usuário registrado com sucesso!";
    }

    // LOGIN
    // LOGIN
    public AuthResponse login(AuthRequest req) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                        req.getPassword()
                )
        );

        UserModel user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));


        String token = jwtService.generateToken(user, user);

        return new AuthResponse(
                token,
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}


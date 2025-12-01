package com.cortexia.cortexia_back_end.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Integer id;
    private String nome;
    private String email;
    private String role;
}
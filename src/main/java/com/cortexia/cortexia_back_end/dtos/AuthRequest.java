package com.cortexia.cortexia_back_end.dtos;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}


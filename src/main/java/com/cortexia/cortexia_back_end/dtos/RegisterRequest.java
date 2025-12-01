package com.cortexia.cortexia_back_end.dtos;

import com.cortexia.cortexia_back_end.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String nome;
    private String email;
    private String password;
    private Role role;
}

package com.cortexia.cortexia_back_end.dtos;

import com.cortexia.cortexia_back_end.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}

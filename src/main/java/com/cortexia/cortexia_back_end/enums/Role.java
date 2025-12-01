package com.cortexia.cortexia_back_end.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    TECNICO,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}

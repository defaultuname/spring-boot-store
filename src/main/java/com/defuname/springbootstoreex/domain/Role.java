package com.defuname.springbootstoreex.domain;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Set.of("USER")), ADMIN(Set.of("USER", "ADMIN"));

    private final Set<String> roles;

    Role(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        return getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }
}

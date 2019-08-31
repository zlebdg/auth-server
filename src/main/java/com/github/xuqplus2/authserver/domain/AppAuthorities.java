package com.github.xuqplus2.authserver.domain;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum AppAuthorities {

    ARTICLE;

    private SimpleGrantedAuthority authority;

    public SimpleGrantedAuthority getAuthority() {
        return authority;
    }

    AppAuthorities() {
        this.authority = new SimpleGrantedAuthority(this.name().toLowerCase());
    }
}

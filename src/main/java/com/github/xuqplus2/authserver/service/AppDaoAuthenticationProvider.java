package com.github.xuqplus2.authserver.service;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;

import javax.transaction.Transactional;

/**
 * 复用<code>DaoAuthenticationProvider</code>的所有代码
 * 并解决 jpa session closed, 不能对关联表进行懒加载的问题
 */
public class AppDaoAuthenticationProvider extends DaoAuthenticationProvider {

    public AppDaoAuthenticationProvider(AppUserDetailsService appUserDetailsService, DelegatingPasswordEncoder delegatingPasswordEncoder) {
        this.setUserDetailsService(appUserDetailsService);
        this.setPasswordEncoder(delegatingPasswordEncoder);
    }

    @Transactional
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return super.authenticate(authentication);
    }
}

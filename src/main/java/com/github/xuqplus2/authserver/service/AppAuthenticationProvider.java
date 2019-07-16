package com.github.xuqplus2.authserver.service;

import com.github.xuqplus2.authserver.domain.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Slf4j
@Component
@Transactional
public class AppAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    EncryptService encryptService;
    @Autowired
    AppUserDetailsService appUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        Object password = authentication.getCredentials();// 明文密码
        AppUser appUser = (AppUser) appUserDetailsService.loadUserByUsername(username);
        String encodedInputPassword = encryptService.encodeAppUserPassword(appUser.getSalt() + password);
        return new UsernamePasswordAuthenticationToken(appUser, encodedInputPassword, appUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        if (aClass.equals(UsernamePasswordAuthenticationToken.class)) {
            return true;
        }
        return false;
    }
}

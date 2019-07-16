package com.github.xuqplus2.authserver.service;

import com.github.xuqplus2.authserver.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.transaction.Transactional;

/**
 * 自定义认证类 AuthenticationProvider
 * <p>
 * 可以在这里重写<code>authenticate</code>方法实现自定义的密码校验方法
 * 成功则返回<code>UsernamePasswordAuthenticationToken</code>
 * 失败则抛出<code>AuthenticationException</code>
 */
//@Component // 不使用自定义认证
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
        if (false) {
            return new UsernamePasswordAuthenticationToken(appUser, password, appUser.getAuthorities());
        }
        throw new BadCredentialsException("密码不正确");
    }

    /**
     * 根据token类型匹配认证类
     * <p>
     * 返回<code>true</code>则执行此类的<code>authenticate</code>方法
     */
    @Override
    public boolean supports(Class<?> aClass) {
//        if (aClass.equals(UsernamePasswordAuthenticationToken.class)) {
//            return true;
//        }
        return false;
    }
}

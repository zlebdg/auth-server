package com.github.xuqplus2.authserver.config.kz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AppRememberMeServices extends PersistentTokenBasedRememberMeServices {

    public AppRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }

    /**
     * 记住登录状态时区分oauth登录类型
     *
     * @param request
     * @param response
     * @param authentication
     */
    @Override
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof RememberMeInfo) {
            RememberMeInfo info = (RememberMeInfo) principal;
            String username = info.getUsername();
            String rememberName = info.getRememberName();
            info.setUsername(rememberName);
            super.onLoginSuccess(request, response, authentication);
            info.setUsername(username);
        } else {
            super.onLoginSuccess(request, response, authentication);
        }
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof RememberMeInfo) {
            String username = ((RememberMeInfo) principal).getUsername();
            ((RememberMeInfo) principal).setUsername(((RememberMeInfo) principal).getRememberName());
            super.logout(request, response, authentication);
        } else {
            super.logout(request, response, authentication);
        }
    }
}

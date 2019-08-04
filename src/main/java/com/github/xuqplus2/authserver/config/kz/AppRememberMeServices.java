package com.github.xuqplus2.authserver.config.kz;

import com.github.xuqplus2.authserver.config.OAuthApp;
import com.github.xuqplus2.authserver.domain.oauth.AlipayUserInfo;
import com.github.xuqplus2.authserver.domain.oauth.GithubUserInfo;
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
        if (principal instanceof GithubUserInfo) {
            GithubUserInfo info = (GithubUserInfo) principal;
            String username = info.getUsername();
            String rememberName = String.format("%s,%s", OAuthApp.GithubApp.class.getSimpleName(), username);
            info.setLogin(rememberName);
            super.onLoginSuccess(request, response, authentication);
            info.setLogin(username);
        } else if (principal instanceof AlipayUserInfo) {
            AlipayUserInfo info = (AlipayUserInfo) principal;
            String username = info.getUsername();
            String rememberName = String.format("%s,%s", OAuthApp.AlipayApp.class.getSimpleName(), username);
            info.setUserId(rememberName);
            super.onLoginSuccess(request, response, authentication);
            info.setUserId(username);
        } else {
            super.onLoginSuccess(request, response, authentication);
        }
    }
}

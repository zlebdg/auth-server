package com.github.xuqplus2.authserver.config.kz;

import com.github.xuqplus2.authserver.config.OAuthApp;
import com.github.xuqplus2.authserver.domain.AppUser;
import com.github.xuqplus2.authserver.domain.oauth.AlipayUserInfo;
import com.github.xuqplus2.authserver.domain.oauth.GithubUserInfo;
import com.github.xuqplus2.authserver.repository.AlipayUserInfoRepository;
import com.github.xuqplus2.authserver.repository.AppUserRepository;
import com.github.xuqplus2.authserver.repository.GithubAccessTokenRepository;
import com.github.xuqplus2.authserver.repository.GithubUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AppUserDetailsService implements UserDetailsService {

    /* oauth 登录记住状态可维持2天 */
    private static final long TOKEN_EXPIRES_MILLS = 1000L * 60 * 60 * 48;

    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    GithubUserInfoRepository githubUserInfoRepository;
    @Autowired
    AlipayUserInfoRepository alipayUserInfoRepository;
    @Autowired
    GithubAccessTokenRepository githubAccessTokenRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.startsWith(OAuthApp.GithubApp.class.getSimpleName())) {
            // GithubApp oauth remember-me
            String oauthUsername = username.split(",")[1];
            GithubUserInfo oauthUser = githubUserInfoRepository.getByLogin(oauthUsername);
            if (null != oauthUser) {
                return oauthUser;
            }
        } else if (username.startsWith(OAuthApp.AlipayApp.class.getSimpleName())) {
            // AlipayApp oauth remember-me
            String oauthUsername = username.split(",")[1];
            AlipayUserInfo oauthUser = alipayUserInfoRepository.getByUserId(oauthUsername);
            if (null != oauthUser && null != oauthUser.getToken()
                    && System.currentTimeMillis() - oauthUser.getToken().getCreateAt() < TOKEN_EXPIRES_MILLS) {
                return oauthUser;
            }
        } else if (username.contains("@")) {
            // AppUser remember-me || AppUser form login
            AppUser appUser = appUserRepository.getByEmail(username);
            if (null != appUser) {
                return appUser;
            }
        } else {
            // AppUser remember-me || AppUser form login
            AppUser appUser = appUserRepository.getByUsername(username);
            if (null != appUser) {
                return appUser;
            }
        }
        throw new UsernameNotFoundException("账号不存在");
    }
}

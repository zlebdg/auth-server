package com.github.xuqplus2.authserver.config.kz;

import com.github.xuqplus2.authserver.config.OAuthApp;
import com.github.xuqplus2.authserver.domain.AppUser;
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
        AppUser appUser = username.contains("@")
                ? appUserRepository.getByEmail(username)
                : appUserRepository.getByUsername(username);
        if (null == appUser) {
            // oauth用户信息
            if (username.startsWith(OAuthApp.GithubApp.class.getSimpleName())) {
                String oauthUsername = username.split(",")[1];
                GithubUserInfo oauthUser = githubUserInfoRepository.getByLogin(oauthUsername);
                if (null != oauthUser) {
                    return oauthUser;
                }
            }
            throw new UsernameNotFoundException("账号不存在");
        }
        return appUser;
    }
}

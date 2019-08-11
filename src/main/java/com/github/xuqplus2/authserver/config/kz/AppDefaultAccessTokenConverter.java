package com.github.xuqplus2.authserver.config.kz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import java.util.Map;

@Slf4j
public class AppDefaultAccessTokenConverter extends DefaultAccessTokenConverter {

    @Override
    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Map map = super.convertAccessToken(token, authentication);
        log.info("map=>{}", map);
        map.put("userInfo", "userInfo");
        return map;
    }
}

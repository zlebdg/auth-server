package com.github.xuqplus2.authserver.config.kz;

import com.github.xuqplus2.authserver.vo.resp.CurrentUser;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import java.util.Map;

public class AppDefaultAccessTokenConverter extends DefaultAccessTokenConverter {

    @Override
    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Map map = super.convertAccessToken(token, authentication);
        map.put("currentUser", new CurrentUser(authentication));
        return map;
    }
}

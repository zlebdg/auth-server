package com.github.xuqplus2.authserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class OAuthApp {

    /**
     * github oauth app
     */
    @Data
    @Component
    @ConfigurationProperties(prefix = "project.oauth.github")
    public static class GithubApp {
        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private String scope;
    }

    /**
     * alipay app
     */
    @Data
    @Component
    @ConfigurationProperties(prefix = "project.oauth.alipay")
    public static class AlipayApp {
        private String alipayGateway;
        private String alipayPublicKey;
        private String appId;
        private String privateKey;
        private String domain;
        private String authCallbackUrl;
        private String scope;
        private String signType;
        private String charset;
        private String format;
    }
}

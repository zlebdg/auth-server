package com.github.xuqplus2.authserver.config;

import com.github.xuqplus2.authserver.config.kz.AppDefaultAccessTokenConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * http://localhost:20010/oauth/authorize?client_id=client&response_type=code&redirect_uri=http://localhost:20008/
 *
 * <p>Basic Y2xpZW50OnNlY3JldA==
 */
@Configuration
@EnableAuthorizationServer
// todo, auth server 重启导致已发出的token全部失效..
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    InMemoryTokenStore inMemoryTokenStore;
    @Autowired
    TokenApprovalStore tokenApprovalStore;
    @Lazy
    @Autowired
    JdbcTokenStore jdbcTokenStore;
    @Autowired
    JdbcApprovalStore jdbcApprovalStore;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JdbcClientDetailsService jdbcClientDetailsService;
    @Autowired
    DataSource dataSource;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /*
        // 使用内存存储OAuth客户端信息
        String secret = "secret";
        String noop = String.format("{noop}%s", secret);
        //    String bcrypt = String.format("{bcrypt}%s", new BCryptPasswordEncoder().encode(secret));
        //    String scrypt = String.format("{scrypt}%s", new SCryptPasswordEncoder().encode(secret));
        clients
                .inMemory()
                *//* client *//*
                .withClient("client") //
                .secret(noop) //
                .authorizedGrantTypes(
                        "password", "authorization_code", "implicit", "refresh_token")
                .resourceIds("resourceId") //
                .redirectUris(
                        "http://aaa.local:20000/login",
                        "http://bbb.local:20000/login",
                        "http://dev.local:20000/login",
                        "http://106.12.80.76:8080/login",
                        "http://106.12.80.76:8081/login",
                        "http://dev.local:20001/login"
                ) //
                .scopes("aaa", "bbb", "ccc", "ddd");*/
        clients
                .withClientDetails(jdbcClientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        /*endpoints
                .tokenStore(inMemoryTokenStore)
                .approvalStore(tokenApprovalStore)
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);*/
        endpoints
                .tokenStore(jdbcTokenStore)
                .approvalStore(jdbcApprovalStore)
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Bean
    public InMemoryTokenStore inMemoryTokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    public TokenApprovalStore approvalStore() {
        TokenApprovalStore store = new TokenApprovalStore();
        store.setTokenStore(inMemoryTokenStore);
        return store;
    }

    @Bean
    @Autowired
    public JdbcTokenStore jdbcTokenStore(DataSource dataSource) {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    @Autowired
    public JdbcApprovalStore jdbcApprovalStore(DataSource dataSource) {
        JdbcApprovalStore jdbcApprovalStore = new JdbcApprovalStore(dataSource);
        return jdbcApprovalStore;
    }

    @Bean
    @Autowired
    public JdbcClientDetailsService jdbcClientDetailsService(DataSource dataSource, DelegatingPasswordEncoder encoder) {
        JdbcClientDetailsService service = new JdbcClientDetailsService(dataSource);
        service.setPasswordEncoder(encoder);
        return service;
    }

    @Autowired
    public void CheckTokenEndpoint(CheckTokenEndpoint endpoint) {
        AppDefaultAccessTokenConverter converter = new AppDefaultAccessTokenConverter();
        endpoint.setAccessTokenConverter(converter);
    }
}

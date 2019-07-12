package com.github.xuqplus2.authserver.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class OAuthAppTest {

    @Test
    public void a() {
        log.info(OAuthApp.GithubApp.class.getSimpleName());
        log.info(OAuthApp.GithubApp.class.getName());
        log.info(OAuthApp.GithubApp.class.getTypeName());
    }
}
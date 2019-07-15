package com.github.xuqplus2.authserver.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@Ignore
public class WebSecurityConfigTest {

    @Test
    public void bcrypt() {
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String aaa = encoder.encode(new StringBuilder("ungWv48Bz+pBQUDeXa4iI7ADYaOWF3qctBD/YfIAFa0="));

        log.info("bcrypt=>{}", aaa);
    }

}
package com.github.xuqplus2.authserver.config;

import com.github.xuqplus2.authserver.AuthServerApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class WebSecurityConfigTest extends AuthServerApplicationTests {

    @Test
    public void bcrypt() {
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String aaa = encoder.encode(new StringBuilder("ungWv48Bz+pBQUDeXa4iI7ADYaOWF3qctBD/YfIAFa0="));

        log.info("bcrypt=>{}", aaa);
    }

    @Autowired
    WebSecurityConfig webSecurityConfig;

    @Test
    public void a() {
        log.info("");
    }
}

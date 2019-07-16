package com.github.xuqplus2.authserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class EncryptConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SCryptPasswordEncoder sCryptPasswordEncoder() {
        return new SCryptPasswordEncoder();
    }

    @Bean
    public StandardPasswordEncoder sha256Encoder() {
        return new StandardPasswordEncoder(); // "sha256"
    }

    @Bean
    public MessageDigestPasswordEncoder md5Encoder() {
        return new MessageDigestPasswordEncoder("MD5");
    }

    @Bean
    @Autowired
    public DelegatingPasswordEncoder delegatingPasswordEncoder(
            BCryptPasswordEncoder bcrypt,
            SCryptPasswordEncoder scrypt,
            StandardPasswordEncoder sha256,
            MessageDigestPasswordEncoder md5) {
        /* springframework 提供的默认密码加密类列表 */
//        final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // 自定义
        final Map<String, PasswordEncoder> encoders = new HashMap<>(1 << 3);
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("bcrypt", bcrypt);
        encoders.put("scrypt", scrypt);
        encoders.put("sha256", sha256);
        encoders.put("md5", md5);
        encoders.put("app", bcrypt);
        return new DelegatingPasswordEncoder("bcrypt", Collections.unmodifiableMap(encoders));
    }
}

package com.github.xuqplus2.authserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.xuqplus2.authserver.domain.AppUser.DEFAULT_PASSWORD_ENCRYPT;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SCryptPasswordEncoder sCryptPasswordEncoder() {
        return new SCryptPasswordEncoder();
    }

    @Bean
    public DelegatingMap encoderMap(BCryptPasswordEncoder bcrypt, SCryptPasswordEncoder scrypt) {
        final Map<String, PasswordEncoder> encoderMap = new LinkedHashMap<>(1 << 3);
        /* encoderMap.put("noop", NoOpPasswordEncoder.getInstance()); // 明文 */
        encoderMap.put("md5", new MessageDigestPasswordEncoder("MD5"));
        encoderMap.put("sha256", new StandardPasswordEncoder()); // "sha256"
        encoderMap.put("bcrypt", bcrypt);
        encoderMap.put("scrypt", scrypt);
        encoderMap.put("app", bcrypt);
        return new DelegatingMap(encoderMap);
    }

    @Bean
    @Autowired
    public DelegatingPasswordEncoder delegatingPasswordEncoder(DelegatingMap delegatingMap) {
        /* springframework 提供的默认密码加密类列表 */
        /* final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder(); */
        return new DelegatingPasswordEncoder(DEFAULT_PASSWORD_ENCRYPT, delegatingMap.getEncoderMap());
    }

    public static class DelegatingMap {

        Map<String, PasswordEncoder> encoderMap;

        public DelegatingMap(Map<String, PasswordEncoder> encoderMap) {
            this.encoderMap = Collections.unmodifiableMap(encoderMap);
        }

        public PasswordEncoder get(String type) {
            if (!this.encoderMap.containsKey(type)) {
                throw new RuntimeException("未实现的加密类型 type=" + type);
            }
            return encoderMap.get(type);
        }

        public Map<String, PasswordEncoder> getEncoderMap() {
            return encoderMap;
        }
    }
}

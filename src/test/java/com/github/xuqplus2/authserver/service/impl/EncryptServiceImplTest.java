package com.github.xuqplus2.authserver.service.impl;

import com.github.xuqplus2.authserver.AuthServerApplicationTests;
import com.github.xuqplus2.authserver.service.EncryptService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class EncryptServiceImplTest extends AuthServerApplicationTests {

    @Autowired
    EncryptService encryptService;

    @Test
    public void sha256() {
        String s = encryptService.sha256("1");
        log.info(s);
        log.info(encryptService.sha256Md5("1"));
        log.info(encryptService.md5(s));
    }
}
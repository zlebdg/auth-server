package com.github.xuqplus2.authserver.service;

import com.github.xuqplus2.authserver.AuthServerApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Ignore
public class EncryptServiceTest extends AuthServerApplicationTests {

    @Autowired
    EncryptService encryptService;

    @Test
    public void sha256() {
        String input = "abc";
        log.info("output=>{}", encryptService.sha256(input));
        log.info("output=>{}", encryptService.sha256(input));

        input = "1111111111111111111111111111111111111111111111111";
        log.info("output=>{}", encryptService.sha256(input));

        input = "1231231231231231231231231231231231312312312312312";
        log.info("output=>{}", encryptService.sha256(input));
    }
}
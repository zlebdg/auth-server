package com.github.xuqplus2.authserver.service;

import com.github.xuqplus2.authserver.AuthServerApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
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

        input = "123456";
        log.info("output=>{}", encryptService.encryptAppUserPassword(input));
        log.info("output=>{}", encryptService.encryptAppUserPassword(input, "noop"));
        log.info("output=>{}", encryptService.encryptAppUserPassword(input, "md5"));
        log.info("output=>{}", encryptService.encryptAppUserPassword(input, "sha256"));
        log.info("output=>{}", encryptService.encryptAppUserPassword(input, "bcrypt"));
        log.info("output=>{}", encryptService.encryptAppUserPassword(input, "scrypt"));
    }
}
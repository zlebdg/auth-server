package com.github.xuqplus2.authserver.service;

import com.github.xuqplus2.authserver.AuthServerApplicationTests;
import com.github.xuqplus2.authserver.domain.AppUser;
import com.github.xuqplus2.authserver.repository.AppUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Ignore
public class AuthServiceTest extends AuthServerApplicationTests {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    EncryptService encryptService;

    /**
     * 各种密码加密码存储方式
     */
    @Test
    public void password() {
        String type = "md5"; // ok
        type = "sha256"; // ok
        type = "scrypt"; // ok
        type = "bcrypt"; // ok
        type = "noop"; // ok
        type = null; // ok
        String username = "445172495@qq.com";
        String password = encryptService.encryptAppUserPassword("123456", type);
        AppUser appUser = appUserRepository.getByUsername(username);
        if (null == appUser) {
            appUser = new AppUser();
            appUser.setUsername(username);
            appUser.setEmail(username);
        }
        appUser.setPassword(password);

        appUserRepository.save(appUser);

        log.info("{}", appUser);
    }
}
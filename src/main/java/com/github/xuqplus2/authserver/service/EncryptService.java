package com.github.xuqplus2.authserver.service;

import com.github.xuqplus2.authserver.config.PasswordEncoderConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static com.github.xuqplus2.authserver.domain.AppUser.DEFAULT_PASSWORD_ENCRYPT;

@Service
@Slf4j
public class EncryptService {

    private static MessageDigest SHA256 = null;

    /* 如有异常直接向上传导使项目启动失败 */
    public EncryptService() throws NoSuchAlgorithmException {
        SHA256 = MessageDigest.getInstance("SHA-256");
    }

    public String sha256(String input) {
        byte[] data = input.getBytes();
        SHA256.update(data);
        return new String(Base64.getEncoder().encode(SHA256.digest()));
    }

    @Autowired
    PasswordEncoderConfig.DelegatingMap delegatingMap;

    public String encryptAppUserPassword(CharSequence password) {
        return this.encryptAppUserPassword(password, DEFAULT_PASSWORD_ENCRYPT);
    }

    public String encryptAppUserPassword(CharSequence password, String type) {
        return String.format("{%s}%s", type, delegatingMap.get(type).encode(password));
    }
}

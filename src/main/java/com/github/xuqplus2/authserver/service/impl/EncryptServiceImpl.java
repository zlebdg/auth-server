package com.github.xuqplus2.authserver.service.impl;

import com.github.xuqplus2.authserver.config.PasswordEncoderConfig;
import com.github.xuqplus2.authserver.service.EncryptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.github.xuqplus2.authserver.domain.AppUser.DEFAULT_PASSWORD_ENCRYPT;

@Service
@Slf4j
public class EncryptServiceImpl implements EncryptService {

    private static MessageDigest SHA256 = null;
    private static MessageDigest MD5 = null;

    @Autowired
    PasswordEncoderConfig.DelegatingMap delegatingMap;

    /* 如有异常直接向上传导使项目启动失败 */
    public EncryptServiceImpl() throws NoSuchAlgorithmException {
        SHA256 = MessageDigest.getInstance("SHA-256");
        MD5 = MessageDigest.getInstance("MD5");
    }

    @Override
    public String md5(String input) {
        return new String(Hex.encode(MD5.digest(input.getBytes())));
    }

    @Override
    public String sha256(String input) {
        return new String(Hex.encode(SHA256.digest(input.getBytes())));
    }

    /**
     * 不等价于 sha256(md5(input))
     *
     * @param input
     * @return
     */
    @Override
    public String sha256Md5(String input) {
        return new String(Hex.encode(MD5.digest(SHA256.digest(input.getBytes()))));
    }

    @Override
    public String encryptAppUserPassword(CharSequence password) {
        return this.encryptAppUserPassword(password, DEFAULT_PASSWORD_ENCRYPT);
    }

    @Override
    public String encryptAppUserPassword(CharSequence password, String type) {
        return String.format("{%s}%s", type, delegatingMap.get(type).encode(password));
    }
}

package com.github.xuqplus2.authserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@Slf4j
public class EncryptService {

    private static MessageDigest SHA256 = null;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SCryptPasswordEncoder sCryptPasswordEncoder;

    @Autowired
    private StandardPasswordEncoder sha256;

    @Autowired
    private MessageDigestPasswordEncoder md5;

    /* 如有异常直接向上传导使项目启动失败 */
    public EncryptService() throws NoSuchAlgorithmException {
        SHA256 = MessageDigest.getInstance("SHA-256");
    }

    public String sha256(String input) {
        byte[] data = input.getBytes();
        SHA256.update(data);
        return new String(Base64.getEncoder().encode(SHA256.digest()));
    }


    public String bCrypt(String input) {
        return bCryptPasswordEncoder.encode(input);
    }

    public String sCrypt(String input) {
        return sCryptPasswordEncoder.encode(input);
    }

    public String encryptAppUserPassword(CharSequence password) {
        return this.encryptAppUserPassword(password, null);
    }

    public String encryptAppUserPassword(CharSequence password, String type) {
        if (null == type || "".equals(type) || "noop".equals(type)) {
            return String.format("{noop}%s", password);
        }
        if ("bcrypt".equals(type)) {
            return String.format("{bcrypt}%s", bCryptPasswordEncoder.encode(password));
        }
        if ("scrypt".equals(type)) {
            return String.format("{scrypt}%s", sCryptPasswordEncoder.encode(password));
        }
        if ("sha256".equals(type)) {
            return String.format("{sha256}%s", sha256.encode(password));
        }
        if ("md5".equals(type)) {
            return String.format("{md5}%s", md5.encode(password));
        }
        throw new RuntimeException("未实现的加密类型 type=" + type);
    }
}

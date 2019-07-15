package com.github.xuqplus2.authserver.service;

import com.mysql.cj.util.Base64Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class EncryptService {

    private static MessageDigest SHA256 = null;
    private static final BASE64Encoder BASE64_ENCODER = new BASE64Encoder();
    private static final Base64Decoder BASE64_DECODER = new Base64Decoder();
    private static final BCryptPasswordEncoder BCRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /* 如有异常直接向上传导使项目启动失败 */
    public EncryptService() throws NoSuchAlgorithmException {
        SHA256 = MessageDigest.getInstance("SHA-256");
    }

    public String sha256(String input) {
        byte[] data = input.getBytes();
        SHA256.update(data);
        return BASE64_ENCODER.encode(SHA256.digest());
    }

    public String encodeAppUserPassword(String input) {
        return BCRYPT_PASSWORD_ENCODER.encode(sha256(input));
    }
}

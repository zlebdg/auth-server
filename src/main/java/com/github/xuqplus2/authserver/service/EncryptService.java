package com.github.xuqplus2.authserver.service;

public interface EncryptService {

    String sha256(String input);

    String encryptAppUserPassword(CharSequence password);

    String encryptAppUserPassword(CharSequence password, String type);
}

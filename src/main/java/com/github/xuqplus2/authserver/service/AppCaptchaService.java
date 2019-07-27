package com.github.xuqplus2.authserver.service;

import com.github.xuqplus2.authserver.exception.CaptchaException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public interface AppCaptchaService {

    // 图形验证码超时时间: 10分钟
    long CAPTCHA_EXPIRED = 1000L * 60 * 10;

    void generate(HttpServletResponse response, HttpSession session) throws IOException;

    void check(String captcha) throws CaptchaException;
}

package com.github.xuqplus2.authserver.controller.test;

import com.google.code.kaptcha.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("auth/test/captcha")
@Slf4j
public class TestCaptchaController {

    @GetMapping()
    public Object a(String text, HttpServletRequest request, HttpSession session) {
        log.info("text={}, now={}", text, System.currentTimeMillis());
        log.info("{}={}, {}={}",
                Constants.KAPTCHA_SESSION_KEY,
                session.getAttribute(Constants.KAPTCHA_SESSION_KEY),
                Constants.KAPTCHA_SESSION_DATE,
                session.getAttribute(Constants.KAPTCHA_SESSION_DATE));

        return text.equals(session.getAttribute(Constants.KAPTCHA_SESSION_KEY));
    }
}

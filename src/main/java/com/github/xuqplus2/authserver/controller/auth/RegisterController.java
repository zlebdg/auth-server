package com.github.xuqplus2.authserver.controller.auth;

import com.google.code.kaptcha.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("register")
@Slf4j
public class RegisterController {

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity register(
            @RequestHeader(value = "User-Agent", required = false) String userAgent,
            @CookieValue("AUTH-SERVER-SESSION-ID") String sessionId,
            @SessionAttribute(Constants.KAPTCHA_SESSION_KEY) String text,
            @SessionAttribute(Constants.KAPTCHA_SESSION_DATE) Long date,
            String username, String email, String captcha, HttpSession session) {
        log.info("userAgent={}, sessionId={}, {}={}, {}={}",
                userAgent, sessionId, Constants.KAPTCHA_SESSION_KEY, text, Constants.KAPTCHA_SESSION_DATE, date);
        log.info("username={}, email={}, captcha={}", username, email, captcha);

        if (captcha.toLowerCase().equals(text.toLowerCase())) {

        }
        return new ResponseEntity("ok", HttpStatus.OK);
    }
}

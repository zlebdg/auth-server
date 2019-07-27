package com.github.xuqplus2.authserver.controller.captcha;

import com.github.xuqplus2.authserver.service.AppCaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("captcha")
public class CaptchaController {

    @Autowired
    AppCaptchaService appCaptchaService;

    @GetMapping
    public void generate(HttpServletResponse response, HttpSession session) throws IOException {
        appCaptchaService.generate(response, session);
    }
}

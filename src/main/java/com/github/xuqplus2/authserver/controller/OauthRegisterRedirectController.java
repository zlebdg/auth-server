package com.github.xuqplus2.authserver.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping
public class OauthRegisterRedirectController {

    @Value("${project.oauth.app.registerUri}")
    String registerUri;

    @GetMapping("/oauth/redirectTo/register")
    public void redirectRegister(HttpServletResponse response) throws IOException {
        response.sendRedirect(registerUri);
    }
}

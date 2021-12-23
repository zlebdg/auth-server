package com.github.xuqplus2.authserver.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

@RestController
@RequestMapping("auth/oauth")
public class OauthRegisterRedirectController {

    @Value("${project.oauth.app.registerUri}")
    String registerUri;

    @Value("${project.oauth.app.resetUri}")
    String resetUri;

    @GetMapping("redirectTo/register")
    public void redirectRegister(HttpServletResponse response) throws IOException {
        if (registerUri.contains("ngrok.io")) {
            URL url = new URL(registerUri);
            response.sendRedirect(String.format("%s#%s", url.getPath(), url.getRef()));
            return;
        }
        response.sendRedirect(registerUri);
    }

    @GetMapping("redirectTo/reset")
    public void redirectReset(HttpServletResponse response) throws IOException {
        if (resetUri.contains("ngrok.io")) {
            URL url = new URL(resetUri);
            response.sendRedirect(String.format("%s#%s", url.getPath(), url.getRef()));
            return;
        }
        response.sendRedirect(resetUri);
    }
}

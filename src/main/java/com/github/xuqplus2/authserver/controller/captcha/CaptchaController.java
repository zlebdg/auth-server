package com.github.xuqplus2.authserver.controller.captcha;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("captcha")
public class CaptchaController {

    @Autowired
    DefaultKaptcha defaultKaptcha;

    @GetMapping
    public void a(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");
        // create the text for the image
        String capText = defaultKaptcha.createText();
        // store the text in the session
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        // store the timestamp in the session
        session.setAttribute(Constants.KAPTCHA_SESSION_DATE, System.currentTimeMillis());
        try {
            // create the image with the text
            BufferedImage bi = defaultKaptcha.createImage(capText);
            // write the data out
            ImageIO.write(bi, "jpg", response.getOutputStream());
            response.getOutputStream().flush();
        } finally {
            response.getOutputStream().close();
        }
    }
}

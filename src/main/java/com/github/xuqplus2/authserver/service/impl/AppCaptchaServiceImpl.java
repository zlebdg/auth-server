package com.github.xuqplus2.authserver.service.impl;

import com.github.xuqplus2.authserver.exception.CaptchaException;
import com.github.xuqplus2.authserver.service.AppCaptchaService;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Slf4j
@Service
public class AppCaptchaServiceImpl implements AppCaptchaService {

    @Autowired
    DefaultKaptcha defaultKaptcha;

    @Override
    public void generate(HttpServletResponse response, HttpSession session) throws IOException {
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

    @Override
    public void check(String captcha) throws CaptchaException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        String key = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        Long date = (Long) session.getAttribute(Constants.KAPTCHA_SESSION_DATE);
        log.info("captcha={}, key={}, date={}, ", captcha, key, date);

        if (StringUtils.isEmpty(captcha)) {
            throw new CaptchaException("图形验证码为空");
        }
        if (null == date || System.currentTimeMillis() - date > CAPTCHA_EXPIRED) {
            throw new CaptchaException("图形验证码超时");
        }
        if (!captcha.equalsIgnoreCase(key)) {
            throw new CaptchaException("图形验证码错误");
        }

        // 验证通过后删除此次验证码
        request.getSession().removeAttribute(Constants.KAPTCHA_SESSION_KEY);
        request.getSession().removeAttribute(Constants.KAPTCHA_SESSION_DATE);
    }
}

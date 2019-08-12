package com.github.xuqplus2.authserver.controller.auth;

import com.github.xuqplus2.authserver.config.kz.AppRememberMeServices;
import com.github.xuqplus2.authserver.service.EncryptService;
import com.github.xuqplus2.authserver.vo.resp.BasicResp;
import com.github.xuqplus2.authserver.vo.resp.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;

@RestController
@RequestMapping("auth")
public class LoginController {

    @Autowired
    AppRememberMeServices appRememberMeServices;
    @Autowired
    JdbcTokenStore jdbcTokenStore;
    @Autowired
    EncryptService encryptService;

    @RequestMapping(value = "currentUser", method = {RequestMethod.GET, RequestMethod.POST}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return BasicResp.ok(new CurrentUser(authentication).toJSONString());
    }

    @CrossOrigin({"http://blog.local:5000", "null", "*"})
    @Transactional
    @RequestMapping(value = "logout", method = {RequestMethod.GET, RequestMethod.POST}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity logout(String accessToken, String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        if (!StringUtils.isEmpty(origin)) {
            response.setHeader("Access-Control-Allow-Credentials", Boolean.TRUE.toString());
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 清除 access token
        if (!StringUtils.isEmpty(accessToken)) {
            jdbcTokenStore.removeAccessToken(accessToken);
        }
        // 清除 refresh token
        if (!StringUtils.isEmpty(refreshToken)) {
            jdbcTokenStore.removeRefreshToken(refreshToken);
        }
        // 清除 remember-me token
        appRememberMeServices.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        // 清除状态
        SecurityContextHolder.clearContext();
        return BasicResp.ok();
    }

    @CrossOrigin({"http://blog.local:5000", "null", "*"})
    @Transactional
    @GetMapping(value = "logout", produces = {MediaType.TEXT_HTML_VALUE})
    public Object logout(String accessToken, String refreshToken, HttpServletRequest request, HttpServletResponse response, ModelAndView mav) throws IOException {
        ResponseEntity logout = this.logout(accessToken, refreshToken, request, response);
        String referer = request.getHeader("Referer");
        if (!StringUtils.isEmpty(referer)) {
            response.sendRedirect(referer);
            return null;
        }
        return logout;
    }
}

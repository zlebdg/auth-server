package com.github.xuqplus2.authserver.controller.auth;

import com.github.xuqplus2.authserver.config.kz.AppRememberMeServices;
import com.github.xuqplus2.authserver.vo.resp.BasicResp;
import com.github.xuqplus2.authserver.vo.resp.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@RestController
@RequestMapping("auth")
public class LoginController {

    @Autowired
    AppRememberMeServices appRememberMeServices;

    @RequestMapping(value = "currentUser", method = {RequestMethod.GET, RequestMethod.POST}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return BasicResp.ok(new CurrentUser(authentication).toJSONString());
    }

    @Transactional
    @RequestMapping(value = "logout", method = {RequestMethod.GET, RequestMethod.POST}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) {
        // 清除 remember-me token
        appRememberMeServices.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        // 清除状态
        SecurityContextHolder.clearContext();
        return BasicResp.ok();
    }
}

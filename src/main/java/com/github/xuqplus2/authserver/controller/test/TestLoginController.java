package com.github.xuqplus2.authserver.controller.test;

import com.github.xuqplus2.authserver.vo.req.Login;
import com.github.xuqplus2.authserver.vo.resp.BasicResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("test/login")
@Slf4j
public class TestLoginController {

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity login(@Valid Login login, BindingResult bindingResult) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        AuthenticationManager authenticationManager = webApplicationContext.getBean(AuthenticationManager.class);
        Authentication token = authenticationManager.authenticate(login.getAuthentication());
        SecurityContextHolder.getContext().setAuthentication(token);
        return BasicResp.ok(token);
    }

    @PostMapping(produces = {MediaType.TEXT_HTML_VALUE})
    public ModelAndView login(@Valid Login login, BindingResult bindingResult, ModelAndView mav) {
        ResponseEntity responseEntity = this.login(login, bindingResult);
        mav.addObject("vo", responseEntity.getBody());
        mav.setViewName("ok");
        return mav;
    }
}

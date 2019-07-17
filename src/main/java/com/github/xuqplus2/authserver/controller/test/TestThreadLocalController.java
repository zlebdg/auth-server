package com.github.xuqplus2.authserver.controller.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("test/threadLocal")
public class TestThreadLocalController {

    @GetMapping("a")
    public String a() {
        return "ok";
    }

    @ModelAttribute
    public void model(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        /**
         * {@link SecurityContextHolderAwareRequestWrapper}
         */
        log.info("request=>{}", request);
        log.info("response=>{}", response);
        log.info("session=>{}", session);
        SecurityContextHolder.getContext();
    }
}

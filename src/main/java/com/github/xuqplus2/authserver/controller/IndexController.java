package com.github.xuqplus2.authserver.controller;

import com.alibaba.fastjson.JSON;
import com.github.xuqplus2.authserver.config.OAuthApp;
import com.github.xuqplus2.authserver.repository.AlipayUserInfoRepository;
import com.github.xuqplus2.authserver.repository.GithubUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class IndexController {

    @Autowired
    GithubUserInfoRepository githubUserInfoRepository;
    @Autowired
    AlipayUserInfoRepository alipayUserInfoRepository;
    @Value("${project.web.index}")
    String index;

    @GetMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect(index);
    }

    @GetMapping("userInfo")
    public String userInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            String[] split = principal.toString().split(",");
            if (OAuthApp.GithubApp.class.getSimpleName().equals(split[0])) {
                return JSON.toJSONString(githubUserInfoRepository.getByLogin(split[1]));
            }
            if (OAuthApp.AlipayApp.class.getSimpleName().equals(split[0])) {
                return JSON.toJSONString(alipayUserInfoRepository.getByUserId(split[1]));
            }
        } else {
            return JSON.toJSONString(principal);
        }
        throw new RuntimeException("没有找到用户信息");
    }
}

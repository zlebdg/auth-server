package com.github.xuqplus2.authserver.controller;

import com.alibaba.fastjson.JSON;
import com.github.xuqplus2.authserver.config.OAuthApp;
import com.github.xuqplus2.authserver.repository.AlipayUserInfoRepository;
import com.github.xuqplus2.authserver.repository.GithubUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AController {

    @Autowired
    GithubUserInfoRepository githubUserInfoRepository;
    @Autowired
    AlipayUserInfoRepository alipayUserInfoRepository;

    @GetMapping("/")
    public String a() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return String.format("授权信息: 名称=%s, 信息=%s, 凭据=%s, 权限=%s, ",
                authentication.getName(),
                authentication.getPrincipal(),
                authentication.getCredentials(),
                authentication.getAuthorities());
    }

    @GetMapping("userInfo")
    public String userInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication.getDetails().toString();
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

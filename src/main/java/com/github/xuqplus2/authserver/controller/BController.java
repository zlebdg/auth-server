package com.github.xuqplus2.authserver.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class BController {

    @RequestMapping("normal")
    public String normal() {
        return "normal";
    }

    @RequestMapping("admin")
    public String admin() {
        return "admin";
    }

    @RequestMapping("root")
    public String root() {
        return "root";
    }

    @RequestMapping("all")
    public String all() {
        return "all";
    }

    @RequestMapping("aaa")
    public String aaa() {
        return String.format("ok, 路径: aaa, 授权名字: %s", SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @RequestMapping("bbb")
    public String bbb() {
        return "bbb";
    }
}

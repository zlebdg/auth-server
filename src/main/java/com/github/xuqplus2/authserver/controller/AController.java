package com.github.xuqplus2.authserver.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AController {

  @GetMapping({"oauth/userInfo", "/"})
  public String userInfo(HttpServletRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return String.format("0=%s, 1=%s, 2=%s, 3=%s, ",
            authentication.getPrincipal(),
            authentication.getAuthorities(),
            authentication.getAuthorities(),
            authentication.getAuthorities());
  }
}

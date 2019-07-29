package com.github.xuqplus2.authserver.controller.auth;

import com.github.xuqplus2.authserver.vo.resp.BasicResp;
import com.github.xuqplus2.authserver.vo.resp.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class LoginController {

    @GetMapping("currentUser")
    public ResponseEntity a() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return BasicResp.ok(new CurrentUser(authentication).toJSONString());
    }
}

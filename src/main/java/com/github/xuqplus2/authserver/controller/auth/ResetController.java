package com.github.xuqplus2.authserver.controller.auth;

import com.github.xuqplus2.authserver.exception.CaptchaException;
import com.github.xuqplus2.authserver.exception.PassswordResetException;
import com.github.xuqplus2.authserver.service.AppCaptchaService;
import com.github.xuqplus2.authserver.service.AuthService;
import com.github.xuqplus2.authserver.vo.req.auth.reset.PasswordReset;
import com.github.xuqplus2.authserver.vo.req.auth.reset.PasswordResetVerify;
import com.github.xuqplus2.authserver.vo.resp.BasicResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("auth/reset")
@Slf4j
public class ResetController {

    static final String REGISTER_VERIFY_URI = "%s/antd/#/antd/user/reset/verify";

    @Autowired
    AuthService authService;

    @Autowired
    AppCaptchaService appCaptchaService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity reset(@Valid PasswordReset reset, BindingResult bindingResult) throws CaptchaException, PassswordResetException {
        if (StringUtils.isEmpty(reset.getVerifyUri())) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String origin = request.getHeader("origin"); // http://192.168.124.95:16000
            if (null == origin) {
                throw new PassswordResetException("参数缺少[verifyUri]或请求头缺少[origin]");
            }
            reset.setVerifyUri(String.format(REGISTER_VERIFY_URI, origin));
            log.info("setVerifyUri origin={}", origin);
        }
        log.info("reset=>{}", reset);
        appCaptchaService.check(reset.getCaptcha());
        authService.reset(reset);
        return BasicResp.ok();
    }

    @PostMapping(value = "verify", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity verify(@Valid PasswordResetVerify verify, BindingResult bindingResult) throws CaptchaException, PassswordResetException {
        authService.resetVerify(verify);
        return BasicResp.ok();
    }
}

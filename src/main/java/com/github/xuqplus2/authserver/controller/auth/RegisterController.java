package com.github.xuqplus2.authserver.controller.auth;

import com.github.xuqplus2.authserver.exception.CaptchaException;
import com.github.xuqplus2.authserver.exception.PasswordNotSetException;
import com.github.xuqplus2.authserver.exception.RegisterException;
import com.github.xuqplus2.authserver.service.AppCaptchaService;
import com.github.xuqplus2.authserver.service.AuthService;
import com.github.xuqplus2.authserver.vo.req.auth.register.Register;
import com.github.xuqplus2.authserver.vo.req.auth.register.RegisterVerify;
import com.github.xuqplus2.authserver.vo.req.auth.register.ResendEmail;
import com.github.xuqplus2.authserver.vo.resp.BasicResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("auth/register")
@Slf4j
public class RegisterController {

    static final String REGISTER_VERIFY_URI = "%s/auth-web/#/auth-web/user/register/verify";

    @Autowired
    AuthService authService;

    @Autowired
    AppCaptchaService appCaptchaService;

    /**
     * Accept: application/json
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity register(@Valid Register register, BindingResult bindingResult) throws RegisterException, CaptchaException {
        // verifyUri被当做一个隐含的参数
        if (StringUtils.isEmpty(register.getVerifyUri())) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String origin = request.getHeader("origin"); // http://192.168.124.95:16000
            if (null == origin) {
                throw new RegisterException("参数缺少[verifyUri]或请求头缺少[origin]");
            }
            register.setVerifyUri(String.format(REGISTER_VERIFY_URI, origin));
            log.info("setVerifyUri origin={}", origin);
//            String host = request.getHeader("host"); // host = dev.local:16000 // 跟 nginx 的 server_name 有关
//            if (!StringUtils.isEmpty(origin)) host = origin;
//            String xForwardedFor = request.getHeader("x-forwarded-for"); // x-forwarded-for = 192.168.124.95
//            if (!StringUtils.isEmpty(xForwardedFor)) host = xForwardedFor;
        }

        log.info("register={}", register);
        appCaptchaService.check(register.getCaptcha());
        authService.register(register);
        return BasicResp.ok(register);
    }

    /**
     * Accept: text/html
     * 天然的多出一个参数, 正好重载 {@link ModelAndView}
     */
    @PostMapping(produces = {MediaType.TEXT_HTML_VALUE, MediaType.ALL_VALUE})
    public ModelAndView register(@Valid Register register, BindingResult bindingResult, ModelAndView mav) throws RegisterException, CaptchaException {
        ResponseEntity responseEntity = this.register(register, bindingResult);
        mav.addObject("vo", responseEntity.getBody());
        mav.setViewName("auth/register");
        return mav;
    }

    /**
     * 验证
     */
    @PostMapping(value = "verify", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity verify(@Valid RegisterVerify verify, BindingResult bindingResult) throws RegisterException, PasswordNotSetException {
        log.info("verify={}", verify);
        authService.registerVerify(verify);
        return BasicResp.ok();
    }

    @GetMapping(value = "verify", produces = MediaType.ALL_VALUE)
    public ModelAndView verify(@Valid RegisterVerify verify, BindingResult bindingResult, ModelAndView mav) throws RegisterException {
        try {
            this.verify(verify, bindingResult);
            mav.setViewName("ok");
            return mav;
        } catch (PasswordNotSetException e) {
            mav.setViewName("auth/verify/setPassword");
            return mav;
        }
    }

    /**
     * 重发邮件
     */
    @PostMapping(value = "resendEmail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerResendEmail(@Valid ResendEmail resendEmail, BindingResult bindingResult) throws RegisterException {
        log.info("resendEmail", resendEmail);
        authService.registerResendEmail(resendEmail);
        return BasicResp.ok();
    }
}

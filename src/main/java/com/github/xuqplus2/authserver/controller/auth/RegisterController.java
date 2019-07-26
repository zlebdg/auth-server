package com.github.xuqplus2.authserver.controller.auth;

import com.github.xuqplus2.authserver.exception.PasswordNotSetException;
import com.github.xuqplus2.authserver.exception.RegisterException;
import com.github.xuqplus2.authserver.exception.VerifiedException;
import com.github.xuqplus2.authserver.service.AuthService;
import com.github.xuqplus2.authserver.vo.req.Register;
import com.github.xuqplus2.authserver.vo.req.RegisterVerify;
import com.github.xuqplus2.authserver.vo.resp.BasicResp;
import com.google.code.kaptcha.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("auth/register")
@Slf4j
public class RegisterController {

    @Autowired
    AuthService authService;

    /**
     * Accept: application/json
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity register(
//            @RequestHeader(value = "Host") String host,
//            @CookieValue("AUTH-SERVER-SESSION-ID") String sessionId, // i.e. 获取cookie值
            @SessionAttribute(Constants.KAPTCHA_SESSION_KEY) String text, // 获取session属性
            @SessionAttribute(Constants.KAPTCHA_SESSION_DATE) Long date,
            @Valid Register register, BindingResult bindingResult) throws RegisterException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

//        String host = request.getHeader("host"); // host = dev.local:16000 // 跟 nginx 的 server_name 有关
        String origin = request.getHeader("origin"); // http://192.168.124.95:16000
//        if (!StringUtils.isEmpty(origin)) host = origin;
//        String xForwardedFor = request.getHeader("x-forwarded-for"); // x-forwarded-for = 192.168.124.95
//        if (!StringUtils.isEmpty(xForwardedFor)) host = xForwardedFor;

        register.setVerifyUri(String.format("%s/auth/register/verify", origin));

        log.info("{}={}, {}={}", Constants.KAPTCHA_SESSION_KEY, text, Constants.KAPTCHA_SESSION_DATE, date);
        log.info("register={}", register);

        authService.register(register, text, date);

        request.getSession().removeAttribute(Constants.KAPTCHA_SESSION_KEY);
        request.getSession().removeAttribute(Constants.KAPTCHA_SESSION_DATE);
        return BasicResp.ok(register);
    }

    /**
     * Accept: text/html
     * 天然的多出一个参数, 正好重载 {@link ModelAndView}
     */
    @PostMapping(produces = {MediaType.TEXT_HTML_VALUE, MediaType.ALL_VALUE})
    public ModelAndView register(
            @SessionAttribute(Constants.KAPTCHA_SESSION_KEY) String text,
            @SessionAttribute(Constants.KAPTCHA_SESSION_DATE) Long date,
            @Valid Register register, BindingResult bindingResult, ModelAndView mav) throws RegisterException {
        ResponseEntity responseEntity = this.register(text, date, register, bindingResult);
        mav.addObject("vo", responseEntity.getBody());
        mav.setViewName("auth/register");
        return mav;
    }

    /**
     * 验证
     */
    @GetMapping(value = "verify", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity verify(@Valid RegisterVerify verify, BindingResult bindingResult) throws RegisterException, PasswordNotSetException, VerifiedException {
        log.info("verify", verify);
        authService.verify(verify);
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
        } catch (VerifiedException e) {
            mav.setViewName("auth/verify/verified");
            return mav;
        }
    }
}

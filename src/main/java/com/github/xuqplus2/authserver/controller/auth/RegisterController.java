package com.github.xuqplus2.authserver.controller.auth;

import com.github.xuqplus2.authserver.service.AuthService;
import com.github.xuqplus2.authserver.service.PasswordNotSetException;
import com.github.xuqplus2.authserver.service.RegisterException;
import com.github.xuqplus2.authserver.service.VerifiedException;
import com.github.xuqplus2.authserver.vo.req.Register;
import com.github.xuqplus2.authserver.vo.req.RegisterVerify;
import com.github.xuqplus2.authserver.vo.resp.BasicResp;
import com.google.code.kaptcha.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
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
            HttpServletRequest request,
            @RequestHeader(value = "Host") String host,
//            @CookieValue("AUTH-SERVER-SESSION-ID") String sessionId, // i.e. 获取cookie值
            @SessionAttribute(Constants.KAPTCHA_SESSION_KEY) String text, // 获取session属性
            @SessionAttribute(Constants.KAPTCHA_SESSION_DATE) Long date,
            @Valid Register register, BindingResult bindingResult) throws RegisterException {
        register.setVerifyUri(String.format("%s://%s/auth/register/verify", request.getScheme(), host));

        log.info("{}={}, {}={}", Constants.KAPTCHA_SESSION_KEY, text, Constants.KAPTCHA_SESSION_DATE, date);
        log.info("register={}", register);

        bindingCheck(bindingResult);
        authService.register(register, text, date);
        return BasicResp.ok(register);
    }

    // 参数检查, todo 改写
    private void bindingCheck(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            throw new RuntimeException(String.format("object [%s]'s field [%s] %s", fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage()));
        }
    }

    /**
     * Accept: text/html
     * 天然的多出一个参数, 正好重载 {@link ModelAndView}
     */
    @PostMapping(produces = {MediaType.TEXT_HTML_VALUE, MediaType.ALL_VALUE})
    public ModelAndView register(
            HttpServletRequest request,
            @RequestHeader(value = "Host") String host,
            @SessionAttribute(Constants.KAPTCHA_SESSION_KEY) String text,
            @SessionAttribute(Constants.KAPTCHA_SESSION_DATE) Long date,
            @Valid Register register, BindingResult bindingResult, ModelAndView mav) throws RegisterException {
        ResponseEntity responseEntity = this.register(request, host, text, date, register, bindingResult);
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
        bindingCheck(bindingResult);
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

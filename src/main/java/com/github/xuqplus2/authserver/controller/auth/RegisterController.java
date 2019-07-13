package com.github.xuqplus2.authserver.controller.auth;

import com.github.xuqplus2.authserver.vo.req.Register;
import com.github.xuqplus2.authserver.vo.resp.RegisterResp;
import com.google.code.kaptcha.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@RequestMapping("auth/register")
@Slf4j
public class RegisterController {

    /**
     * Accept: application/json
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RegisterResp register(
//            @RequestHeader(value = "User-Agent", required = false) String userAgent, // i.e. 获取header值
//            @CookieValue("AUTH-SERVER-SESSION-ID") String sessionId, // i.e. 获取cookie值
            @SessionAttribute(Constants.KAPTCHA_SESSION_KEY) String text, // 获取session属性
            @SessionAttribute(Constants.KAPTCHA_SESSION_DATE) Long date,
            @Valid Register register, BindingResult bindingResult) {
        log.info("{}={}, {}={}", Constants.KAPTCHA_SESSION_KEY, text, Constants.KAPTCHA_SESSION_DATE, date);
        log.info("register={}", register);

        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            throw new RuntimeException(String.format("object [%s]'s field [%s] %s", fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage()));
        }
        if (register.getCaptcha().equalsIgnoreCase(text)) {

        }
        return RegisterResp
                .builder()
                .code(200)
                .message("ok")
                .data(register)
                .build();
    }

    /**
     * Accept: text/html
     * 天然的多出一个参数, 正好重载 {@link ModelAndView}
     */
    @PostMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView register(
            @SessionAttribute(Constants.KAPTCHA_SESSION_KEY) String text,
            @SessionAttribute(Constants.KAPTCHA_SESSION_DATE) Long date,
            @Valid Register register, BindingResult bindingResult, ModelAndView mav) {
        mav.addObject("vo", this.register(text, date, register, bindingResult));
        mav.setViewName("auth/register");
        return mav;
    }
}

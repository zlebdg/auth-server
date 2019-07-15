package com.github.xuqplus2.authserver.service;

import com.github.xuqplus2.authserver.domain.AppRegister;
import com.github.xuqplus2.authserver.domain.AppUser;
import com.github.xuqplus2.authserver.listener.AppRegisterEvent;
import com.github.xuqplus2.authserver.repository.AppRegisterRepository;
import com.github.xuqplus2.authserver.repository.AppUserRepository;
import com.github.xuqplus2.authserver.vo.req.Register;
import com.github.xuqplus2.authserver.vo.req.RegisterVerify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

@Service
@Slf4j
public class AuthService {

    private static final long CAPTCHA_EXPIRED = 1000L * 60 * 10;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    AppRegisterRepository appRegisterRepository;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    public void register(Register register, String text, Long date) throws RegisterException {
        if (System.currentTimeMillis() - date > CAPTCHA_EXPIRED) {
            throw new RegisterException("图形验证码超时");
        }
        if (!register.getCaptcha().equalsIgnoreCase(text)) {
            throw new RegisterException("图形验证码错误");
        }
        // 用户名/邮箱格式检查
        // todo
        if (appUserRepository.existsByUsername(register.getUsername())
                || appRegisterRepository.existsByUsername(register.getUsername())) {
            throw new RegisterException("用户名已经注册");
        }
        if (appUserRepository.existsByEmail(register.getEmail())
                || appRegisterRepository.existsByEmail(register.getEmail())) {
            throw new RegisterException("邮箱已经注册");
        }
        AppRegister appRegister = new AppRegister(register);
        appRegisterRepository.save(appRegister);
        // 发布事件
        eventPublisher.publishEvent(new AppRegisterEvent(appRegister));
    }

    @Transactional
    public void verify(RegisterVerify verify) throws RegisterException, PasswordNotSetException, VerifiedException {
        AppRegister register = appRegisterRepository.getByUsername(verify.getUsername());
        if (null == register) {
            throw new RegisterException("没有查到注册信息");
        }
        if (register.getIsDeleted()) {
            throw new VerifiedException();
        }
        if (!verify.getVerifyCode().equalsIgnoreCase(register.getVerifyCode())) {
            throw new RegisterException("验证失败");
        }
        if (StringUtils.isEmpty(verify.getPassword())) {
            throw new PasswordNotSetException();
        }
        AppUser appUser = new AppUser(register, verify);
        appUserRepository.save(appUser);
        register.setIsDeleted(true);
        appRegisterRepository.save(register);
        log.info("注册完成, username=>{}", appUser.getUsername());
    }
}

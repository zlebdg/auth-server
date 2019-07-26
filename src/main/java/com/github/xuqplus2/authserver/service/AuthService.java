package com.github.xuqplus2.authserver.service;

import com.github.xuqplus2.authserver.domain.AppRegister;
import com.github.xuqplus2.authserver.domain.AppUser;
import com.github.xuqplus2.authserver.exception.PasswordNotSetException;
import com.github.xuqplus2.authserver.exception.RegisterException;
import com.github.xuqplus2.authserver.exception.VerifiedException;
import com.github.xuqplus2.authserver.listener.AppRegisterEvent;
import com.github.xuqplus2.authserver.repository.AppRegisterRepository;
import com.github.xuqplus2.authserver.repository.AppUserRepository;
import com.github.xuqplus2.authserver.vo.req.Register;
import com.github.xuqplus2.authserver.vo.req.RegisterVerify;
import com.github.xuqplus2.authserver.vo.req.register.ResendEmail;
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
    private static final long REGISTER_EVENT_PUBLISH_INTERVAL = 1000L * 60;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    AppRegisterRepository appRegisterRepository;

    @Autowired
    EncryptService encryptService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    public void register(Register register, String text, Long date) throws RegisterException {
        if (System.currentTimeMillis() - date > CAPTCHA_EXPIRED) {
            throw new RegisterException("图形验证码超时");
        }
        if (!register.getCaptcha().equalsIgnoreCase(text)) {
            throw new RegisterException("图形验证码错误");
        }
        if (appUserRepository.existsByUsername(register.getUsername())) {
            throw new RegisterException("用户名已经注册");
        }
        if (appUserRepository.existsByEmail(register.getEmail())) {
            throw new RegisterException("邮箱已经注册");
        }
        if (appRegisterRepository.existsByUsername(register.getUsername())) {
            throw new RegisterException("用户名已经注册");
        }
        if (appRegisterRepository.existsByEmail(register.getEmail())) {
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
        appUser.setNewPassword(appUser.getPassword(), encryptService);
        appUserRepository.save(appUser);
        register.setIsDeleted(true);
        appRegisterRepository.save(register);
        log.info("注册完成, username=>{}", appUser.getUsername());
    }

    public void resendEmail(ResendEmail resendEmail) throws RegisterException {
        AppRegister appRegister = appRegisterRepository.getByUsernameAndEmailAndIsDeletedFalse(resendEmail.getUsername(), resendEmail.getEmail());
        if (null != appRegister) {
            // 邮件重发间隔
            if ((null == appRegister.getUpdateAt() && System.currentTimeMillis() - appRegister.getCreateAt() > REGISTER_EVENT_PUBLISH_INTERVAL)
                    || (null != appRegister.getUpdateAt() && System.currentTimeMillis() - appRegister.getUpdateAt() > REGISTER_EVENT_PUBLISH_INTERVAL)) {
                appRegister.refreshVerifyCode();
                appRegisterRepository.save(appRegister);
                // 发布事件
                eventPublisher.publishEvent(new AppRegisterEvent(appRegister));
                return;
            } else {
                throw new RegisterException("未满足邮件重发间隔");
            }
        }
        throw new RegisterException("注册信息不存在");
    }
}

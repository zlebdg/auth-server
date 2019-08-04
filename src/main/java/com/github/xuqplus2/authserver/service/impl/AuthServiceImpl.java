package com.github.xuqplus2.authserver.service.impl;

import com.github.xuqplus2.authserver.domain.AppPasswordReset;
import com.github.xuqplus2.authserver.domain.AppRegister;
import com.github.xuqplus2.authserver.domain.AppUser;
import com.github.xuqplus2.authserver.exception.PassswordResetException;
import com.github.xuqplus2.authserver.exception.PasswordNotSetException;
import com.github.xuqplus2.authserver.exception.RegisterException;
import com.github.xuqplus2.authserver.listener.AppPasswordResetEvent;
import com.github.xuqplus2.authserver.listener.AppRegisterEvent;
import com.github.xuqplus2.authserver.repository.AppPasswordResetRepository;
import com.github.xuqplus2.authserver.repository.AppRegisterRepository;
import com.github.xuqplus2.authserver.repository.AppUserRepository;
import com.github.xuqplus2.authserver.service.AuthService;
import com.github.xuqplus2.authserver.service.EncryptService;
import com.github.xuqplus2.authserver.vo.req.auth.register.Register;
import com.github.xuqplus2.authserver.vo.req.auth.register.RegisterVerify;
import com.github.xuqplus2.authserver.vo.req.auth.register.ResendEmail;
import com.github.xuqplus2.authserver.vo.req.auth.reset.PasswordReset;
import com.github.xuqplus2.authserver.vo.req.auth.reset.PasswordResetVerify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;

import javax.transaction.Transactional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    AppRegisterRepository appRegisterRepository;
    @Autowired
    AppPasswordResetRepository appPasswordResetRepository;
    @Autowired
    PersistentTokenRepository persistentTokenRepository;
    @Autowired
    EncryptService encryptService;
    @Autowired
    DelegatingPasswordEncoder delegatingPasswordEncoder;
    @Autowired
    ApplicationEventPublisher eventPublisher;

    public void register(Register register) throws RegisterException {
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
    public void registerVerify(RegisterVerify verify) throws RegisterException, PasswordNotSetException {
        String[] verifyCode = verify.getVerifyCode().split(";");
        String username = verifyCode[0];
        String encryptCode = verifyCode[1];
        AppRegister register = appRegisterRepository.getByUsername(username);
        if (null == register) {
            throw new RegisterException("没有查到注册信息");
        }
        if (register.getIsDeleted()) {
            return; // 幂等处理
        }
        if (register.isExpired()) {
            throw new RegisterException("注册申请过期");
        }
        if (!delegatingPasswordEncoder.matches(register.getVerifyCode(), encryptCode)) {
            throw new RegisterException("验证失败");
        }
        if (StringUtils.isEmpty(verify.getPassword())) {
            throw new PasswordNotSetException("没有设置密码");
        }
        AppUser appUser = new AppUser(register, verify);
        appUser.setNewPassword(appUser.getPassword(), encryptService);
        appUserRepository.save(appUser);
        register.setIsDeleted(true);
        appRegisterRepository.save(register);
        log.info("注册完成, username=>{}", appUser.getUsername());
    }

    public void registerResendEmail(ResendEmail resendEmail) throws RegisterException {
        AppRegister appRegister = appRegisterRepository.getByUsernameAndEmailAndIsDeletedFalse(resendEmail.getUsername(), resendEmail.getEmail());
        if (null != appRegister) {
            // 邮件重发间隔
            if ((null == appRegister.getUpdateAt() && System.currentTimeMillis() - appRegister.getCreateAt() > REGISTER_EMAIL_SENDING_INTERVAL)
                    || (null != appRegister.getUpdateAt() && System.currentTimeMillis() - appRegister.getUpdateAt() > REGISTER_EMAIL_SENDING_INTERVAL)) {
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

    @Override
    public void reset(PasswordReset reset) throws PassswordResetException {
        if (!StringUtils.isEmpty(reset.getEmail())) {
            AppUser user = appUserRepository.getByEmail(reset.getEmail());
            if (null == user) {
                throw new PassswordResetException("邮箱不存在");
            }
            reset.setUsername(user.getUsername());
            AppPasswordReset passwordReset = new AppPasswordReset(reset);
            appPasswordResetRepository.save(passwordReset);
            AppPasswordResetEvent event = new AppPasswordResetEvent(passwordReset);
            // 发布重置密码事件
            eventPublisher.publishEvent(event);
            return;
        }
        if (!StringUtils.isEmpty(reset.getUsername())) {
            AppUser user = appUserRepository.getByUsername(reset.getUsername());
            if (null == user) {
                throw new PassswordResetException("用户名不存在");
            }
            reset.setEmail(user.getEmail());
            AppPasswordReset passwordReset = new AppPasswordReset(reset);
            appPasswordResetRepository.save(passwordReset);
            AppPasswordResetEvent event = new AppPasswordResetEvent(passwordReset);
            // 发布重置密码事件
            eventPublisher.publishEvent(event);
            return;
        }
        throw new PassswordResetException("密码重置失败, 没有查到账号");
    }

    @Override
    @Transactional
    public void resetVerify(PasswordResetVerify verify) throws PassswordResetException {
        String[] verifyCode = verify.getVerifyCode().split(";");
        String username = verifyCode[0];
        String encryptCode = verifyCode[1];
        AppPasswordReset reset = appPasswordResetRepository.getByUsername(username);
        if (null == reset) {
            throw new PassswordResetException("没有密码重置申请记录");
        }
        if (reset.getIsDeleted()) {
            return; // 幂等处理
        }
        if (reset.isExpired()) {
            throw new PassswordResetException("密码重置申请过期");
        }
        if (StringUtils.isEmpty(verify.getPassword())) {
            throw new PassswordResetException("缺少[password]参数");
        }
        if (delegatingPasswordEncoder.matches(reset.getVerifyCode(), encryptCode)) {
            AppUser user = appUserRepository.getByUsername(username);
            user.setNewPassword(verify.getPassword(), encryptService);
            appUserRepository.save(user);
            reset.setIsDeleted(true);
            appPasswordResetRepository.save(reset);
            // 重置完成后， 清除 remember-me token
            persistentTokenRepository.removeUserTokens(username);
            // && 清除当前登录状态
            SecurityContextHolder.getContext().setAuthentication(null);
            return;
        }
        throw new PassswordResetException("设置新密码失败");
    }
}

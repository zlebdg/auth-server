package com.github.xuqplus2.authserver.service;

import com.github.xuqplus2.authserver.exception.PassswordResetException;
import com.github.xuqplus2.authserver.exception.PasswordNotSetException;
import com.github.xuqplus2.authserver.exception.RegisterException;
import com.github.xuqplus2.authserver.vo.req.auth.register.Register;
import com.github.xuqplus2.authserver.vo.req.auth.register.RegisterVerify;
import com.github.xuqplus2.authserver.vo.req.auth.register.ResendEmail;
import com.github.xuqplus2.authserver.vo.req.auth.reset.PasswordReset;
import com.github.xuqplus2.authserver.vo.req.auth.reset.PasswordResetVerify;

public interface AuthService {

    // 注册验证邮件重发间隔
    long REGISTER_EMAIL_SENDING_INTERVAL = 1000L * 60;

    void register(Register register) throws RegisterException;

    void registerVerify(RegisterVerify verify) throws RegisterException, PasswordNotSetException;

    void registerResendEmail(ResendEmail resendEmail) throws RegisterException;

    void reset(PasswordReset reset) throws PassswordResetException;

    void resetVerify(PasswordResetVerify verify) throws PassswordResetException;
}

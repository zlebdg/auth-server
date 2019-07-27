package com.github.xuqplus2.authserver.service;

import com.github.xuqplus2.authserver.exception.PassswordResetException;
import com.github.xuqplus2.authserver.exception.PasswordNotSetException;
import com.github.xuqplus2.authserver.exception.RegisterException;
import com.github.xuqplus2.authserver.exception.VerifiedException;
import com.github.xuqplus2.authserver.vo.req.auth.register.Register;
import com.github.xuqplus2.authserver.vo.req.auth.register.RegisterVerify;
import com.github.xuqplus2.authserver.vo.req.auth.register.ResendEmail;
import com.github.xuqplus2.authserver.vo.req.auth.reset.PassswordReset;

public interface AuthService {

    long REGISTER_EVENT_PUBLISH_INTERVAL = 1000L * 60;

    void register(Register register) throws RegisterException;

    void registerVerify(RegisterVerify verify) throws RegisterException, PasswordNotSetException, VerifiedException;

    void registerResendEmail(ResendEmail resendEmail) throws RegisterException;

    void reset(PassswordReset reset) throws PassswordResetException;
}

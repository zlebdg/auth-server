package com.github.xuqplus2.authserver.scheduled;

import com.github.xuqplus2.authserver.domain.AppRegister;
import com.github.xuqplus2.authserver.repository.AppPasswordResetRepository;
import com.github.xuqplus2.authserver.repository.AppRegisterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Slf4j
@Component
public class AuthTask {

    @Autowired
    AppRegisterRepository appRegisterRepository;

    @Autowired
    AppPasswordResetRepository appPasswordResetRepository;

    @Transactional
    @Scheduled(fixedDelay = 1000L * 60)
    public void cleanDeleteDAndExpiredAppRegister() {
//        log.info("{} task start", Thread.currentThread().getStackTrace()[1].getMethodName());
        // 过期的
        appRegisterRepository.deleteByCreateAtLessThan(System.currentTimeMillis() - AppRegister.EXPIRED_TIME_MILLS);
        // 验证过的
        appRegisterRepository.deleteByCreateAtLessThanAndIsDeletedTrue(System.currentTimeMillis() - 1000L * 60);
//        log.info("{} task done", Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Transactional
    @Scheduled(fixedDelay = 1000L * 60)
    public void cleanDeleteDAndExpiredAppPasswordReset() {
//        log.info("{} task start.", Thread.currentThread().getStackTrace()[1].getMethodName());
        // 过期的
        appPasswordResetRepository.deleteByCreateAtLessThan(System.currentTimeMillis() - AppRegister.EXPIRED_TIME_MILLS);
        // 重置过的
        appPasswordResetRepository.deleteByCreateAtLessThanAndIsDeletedTrue(System.currentTimeMillis() - 1000L * 60);
//        log.info("{} task done", Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}

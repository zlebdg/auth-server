package com.github.xuqplus2.authserver.listener;

import com.github.xuqplus2.authserver.domain.AppPasswordReset;
import com.github.xuqplus2.authserver.service.EncryptService;
import com.github.xuqplus2.authserver.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

@Async
@Component
@Slf4j
public class AppPasswordResetListener implements ApplicationListener<AppPasswordResetEvent> {

    public static final String SUBJECT_TEMPLATE = "浦江xuqplus2-%s 你刚刚申请了密码重置";
    public static final String TEXT_TEMPLATE = "打开链接(%s?verifyCode=%s)完成密码重置\n如果不能直接点击, 请复制此链接并粘贴到地址栏访问, 如非本人注册请忽略此邮件";
    public static final String URI_TEMPLATE = "%s?username=%s&verifyCode=%s";

    @Value("${project.profile}")
    String profile;

    @Autowired
    MailService mailService;

    @Autowired
    EncryptService encryptService;

    @Override
    public void onApplicationEvent(AppPasswordResetEvent event) {
        AppPasswordReset reset = (AppPasswordReset) event.getSource();
        log.info("reset=>{}", reset);
        String subject = String.format(SUBJECT_TEMPLATE, profile);

        String verifyCode = URLEncoder.encode(String.format("%s;%s", reset.getUsername(),
                encryptService.encryptAppUserPassword(reset.getVerifyCode())));

        String text = String.format(TEXT_TEMPLATE, reset.getVerifyUri(), verifyCode);
        String to = reset.getEmail();
        mailService.sendSimpleMailMessage(subject, text, to);
    }
}

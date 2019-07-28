package com.github.xuqplus2.authserver.listener;

import com.github.xuqplus2.authserver.domain.AppRegister;
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
public class AppRegisterListener implements ApplicationListener<AppRegisterEvent> {

    public static final String SUBJECT_TEMPLATE = "浦江xuqplus2-%s 你刚刚注册了新账号";
    public static final String TEXT_TEMPLATE = "打开链接(%s?verifyCode=%s)完成本次注册\n如果不能直接点击, 请复制此链接并粘贴到地址栏访问, 如非本人注册请忽略此邮件";

    @Value("${project.profile}")
    String profile;

    @Autowired
    MailService mailService;

    @Autowired
    EncryptService encryptService;

    @Override
    public void onApplicationEvent(AppRegisterEvent event) {
        AppRegister appRegister = (AppRegister) event.getSource();
        log.info("appRegister=>{}", appRegister);
        String subject = String.format(SUBJECT_TEMPLATE, profile);

        String verifyCode = URLEncoder.encode(String.format("%s;%s", appRegister.getUsername(),
                encryptService.encryptAppUserPassword(appRegister.getVerifyCode())));

        String text = String.format(TEXT_TEMPLATE, appRegister.getVerifyUri(), verifyCode);
        String to = appRegister.getEmail();
        mailService.sendSimpleMailMessage(subject, text, to);
    }
}

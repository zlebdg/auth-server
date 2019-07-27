package com.github.xuqplus2.authserver.service.impl;

import com.github.xuqplus2.authserver.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    @Autowired
    JavaMailSenderImpl javaMailSender;
    @Value("${postfix.username}")
    String from;

    public void sendSimpleMailMessage(String subject, String text, String... to) {
        javaMailSender.send(this.createSimpleMailMessage(subject, text, from, to));
        log.info("邮件已经发出, subject={}, text={}, from={}, to={}", subject, text, from, to);
    }

    public MimeMessage createMimeMessage(String subject, String text, String from, String... to)
            throws MessagingException {
        // 使用JavaMail的MimeMessage，支付更加复杂的邮件格式和内容
        // MimeMessages为复杂邮件模板，支持文本、附件、html、图片等。
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text);
        // mimeMessageHelper.addAttachment("resource", resource);
        return mimeMessage;
    }

    public SimpleMailMessage createSimpleMailMessage(
            String subject, String text, String from, String... to) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        return simpleMailMessage;
    }
}

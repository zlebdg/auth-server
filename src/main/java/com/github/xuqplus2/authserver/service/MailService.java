package com.github.xuqplus2.authserver.service;

import org.springframework.mail.SimpleMailMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface MailService {

    void sendSimpleMailMessage(String subject, String text, String... to);

    MimeMessage createMimeMessage(String subject, String text, String from, String... to) throws MessagingException;

    SimpleMailMessage createSimpleMailMessage(String subject, String text, String from, String... to);
}

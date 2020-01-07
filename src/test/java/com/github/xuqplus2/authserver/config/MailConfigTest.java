package com.github.xuqplus2.authserver.config;

import com.github.xuqplus2.authserver.AuthServerApplicationTests;
import com.github.xuqplus2.authserver.service.MailService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailConfigTest extends AuthServerApplicationTests {

	@Autowired
	JavaMailSenderImpl mailSender;

	@Autowired
	MailService mailService;

	@Test
	public void javaMailSender() {
		String subject = "subject";
		String text = "text, hello 世界, 123abc ..";
		String receiver = "445172495@qq.com";
		mailService.sendSimpleMailMessage(subject, text, receiver);
	}
}
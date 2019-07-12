package com.github.xuqplus2.authserver.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Configuration
public class MailConfig {

    @Bean
    @Autowired
    public JavaMailSenderImpl javaMailSender(MailProperties properties) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setDefaultEncoding("utf-8");
        javaMailSender.setHost(properties.getHost());
        javaMailSender.setPort(properties.getPort());
        javaMailSender.setProtocol(properties.getProtocol());
        javaMailSender.getJavaMailProperties().setProperty("mail.smtp.localhost", properties.getHost());
        if (StringUtils.isEmpty(properties.getUsername()) && StringUtils.isEmpty(properties.getPassword())) {
            // none
        } else {
            javaMailSender.setUsername(properties.getUsername());
            javaMailSender.setPassword(properties.getPassword());
        }
        return javaMailSender;
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "postfix")
    public static class MailProperties {
        String host;
        Integer port;
        String protocol;
        String username;
        String password;
    }
}

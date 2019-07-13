package com.github.xuqplus2.authserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 写这个类的时候是在配置 KaptchaConfig
 * 为了避免重复写类似 get/set(com.google.code.kaptcha.Constants.KAPTCHA_BORDER) 的代码, 于是将全部
 * application.properties读到这个类里, 然后properties全部set到DefaultKaptcha中, 主DefaultKaptcha自己按需读取
 * <p>
 * 如有其他Config也有按需读取的能力, 则同样可以复用此类
 * <p>
 * todo, MailConfig可以改写..
 */
@Component
@ConfigurationProperties
public class ApplicationProperties extends Properties {
}

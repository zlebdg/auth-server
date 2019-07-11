package com.github.xuqplus2.authserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .formLogin().and()
            .authorizeRequests()
            .antMatchers("/", "/all", "/index.html", "/login**").permitAll()
            .antMatchers("/oauth/**").permitAll()
            .antMatchers("/normal").hasRole("normal")
            .antMatchers("/admin").hasRole("admin")
            .antMatchers("/root").hasRole("root")
            .anyRequest().authenticated().and()
            .csrf().disable();
  }

  @Autowired
  public void configBuilder(AuthenticationManagerBuilder builder) throws Exception {
    final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    builder
            .inMemoryAuthentication().passwordEncoder(encoder)
            // role会转换成权限ROLE_${role}, 设置了权限时role会被忽略
            .withUser("normal").password(encoder.encode("123456")).roles("normal").authorities("normal2").and()
            .withUser("admin").password(encoder.encode("123456")).roles("admin").and()
            .withUser("root").password(encoder.encode("123456")).roles("root");
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}

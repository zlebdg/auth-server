package com.github.xuqplus2.authserver.config;

import com.github.xuqplus2.authserver.config.kz.AppDaoAuthenticationProvider;
import com.github.xuqplus2.authserver.config.kz.AppRememberMeServices;
import com.github.xuqplus2.authserver.config.kz.AppUserDetailsService;
import com.github.xuqplus2.authserver.service.EncryptService;
import com.github.xuqplus2.authserver.vo.resp.BasicResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AppDaoAuthenticationProvider appDaoAuthenticationProvider;
    @Autowired
    DelegatingPasswordEncoder delegatingPasswordEncoder;
    @Autowired
    EncryptService encryptService;
    @Autowired
    PersistentTokenRepository persistentTokenRepository;
    @Autowired
    AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    ContentNegotiationManager contentNegotiationManager;
    @Autowired
    StringHttpMessageConverter stringHttpMessageConverter;
    @Autowired
    AppRememberMeServices appRememberMeServices;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .oauth2Login().loginPage("/login.html").and() // todo to be known
//                .openidLogin().loginPage("/login.html").and() // todo to be known
                .formLogin()
                .loginProcessingUrl("/login")
                .loginPage("/login.html") // 重写登录网页
                .successHandler(authenticationSuccessHandler) // text/html,... | application/json(utf8)
                .failureHandler(authenticationFailureHandler) // text/html,... | application/json(utf8)
                .and()
                .authorizeRequests()
                .antMatchers("/all",
                        "/**/**.html",
                        "/test**/**",
                        "/oauth/**",
                        "/auth/**/**",
                        "/captcha**",
                        "/").permitAll()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/normal").hasRole("normal")
                .antMatchers("/admin").hasRole("admin")
                .antMatchers("/root").hasRole("root")
                .anyRequest().authenticated().and()
                // token 持久化
                .rememberMe()
                .tokenRepository(persistentTokenRepository)
                .rememberMeServices(appRememberMeServices)
                // TokenBasedRememberMeServices生成一个RememberMeAuthenticationToken,由RememberMeAuthenticationProvider处理。一个key认证提供者和TokenBasedRememberMeServices之间共享。
                // ref: https://springcloud.cc/spring-security-zhcn.html#remember-me
                .key(appRememberMeServices.getKey())
                .and()
                .csrf().disable()
        ;
    }

    // role会转换成权限ROLE_${role}, 设置了权限时role会被忽略
    @Autowired
    public void configBuilder(AuthenticationManagerBuilder builder) throws Exception {
        builder
//                /**
//                 * 会将<code>appUserDetailsService</code>注册到
//                 * <code>ProviderManager</code>的
//                 * <code>List<AuthenticationProvider> providers</code>的里
//                 * 实现从数据库读取用户信息
//                 */
//                .userDetailsService(appUserDetailsService).passwordEncoder(delegatingPasswordEncoder).and()
                /**
                 * 登录认证实现
                 */
                .authenticationProvider(appDaoAuthenticationProvider)
                /**
                 * 因为<code>ProviderManager</code>的<code>providers</code>是<code>List</code>类型
                 * 所以认证方法的执行是须序有关的
                 * 先设置<code>appUserDetailsService</code>
                 * 从而在登录认证时, 先读取数据库用户信息, 后读取内存用户信息
                 */
                .inMemoryAuthentication()
                .passwordEncoder(delegatingPasswordEncoder)
                .withUser("test").password(encryptService.encryptAppUserPassword("123456")).roles("test").and()
                .withUser("root").password(encryptService.encryptAppUserPassword("123456")).roles("root");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Autowired
    public AppDaoAuthenticationProvider appDaoAuthenticationProvider(AppUserDetailsService appUserDetailsService, DelegatingPasswordEncoder delegatingPasswordEncoder) {
        return new AppDaoAuthenticationProvider(appUserDetailsService, delegatingPasswordEncoder);
    }

    /**
     * 期望antd前端登录成功后, 响应的header里不出现<code>302 Location 重定向</code>
     *
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        // 继承缺省实现, 重写关键方法
        SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                List<MediaType> mediaTypes = contentNegotiationManager.resolveMediaTypes(new ServletWebRequest(request));
                if (mediaTypes.contains(MediaType.APPLICATION_JSON) || mediaTypes.contains(MediaType.APPLICATION_JSON_UTF8)) {
                    request.getRequestDispatcher("/auth/currentUser").forward(request, response);
                } else {
                    super.onAuthenticationSuccess(request, response, authentication);
                }
            }
        };
        return handler;
    }

    /**
     * 同样, 期望antd前端登录失败时, 响应的header里不出现<code>302 Location 重定向</code>
     *
     * @return
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        // 继承缺省实现类, 重写关键方法
        SimpleUrlAuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler() {

            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                List<MediaType> mediaTypes = contentNegotiationManager.resolveMediaTypes(new ServletWebRequest(request));
                if (true || mediaTypes.contains(MediaType.APPLICATION_JSON) || mediaTypes.contains(MediaType.APPLICATION_JSON_UTF8)) {
                    log.info(exception.getMessage());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    stringHttpMessageConverter.write(new BasicResp(HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), null).toJSONString(), MediaType.APPLICATION_JSON_UTF8, new ServletServerHttpResponse(response));
                } else {
                    super.onAuthenticationFailure(request, response, exception);
                }
            }
        };
        return handler;
    }
}

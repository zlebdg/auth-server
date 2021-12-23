package com.github.xuqplus2.authserver.controller.oauth.callback;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.github.xuqplus2.authserver.config.OAuthApp;
import com.github.xuqplus2.authserver.config.kz.AppRememberMeServices;
import com.github.xuqplus2.authserver.domain.oauth.*;
import com.github.xuqplus2.authserver.repository.*;
import com.github.xuqplus2.authserver.util.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.jaas.JaasAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("auth/oauth/callback")
@Slf4j
public class OAuthCallbackController {

    /**
     * todo, 第三方oauth登录授予基础权限
     */
    private static final List<GrantedAuthority> BASIC_AUTHORITIES = new ArrayList<>(1);
    private static final SimpleGrantedAuthority BASIC = new SimpleGrantedAuthority("BASIC");

    static {
        BASIC_AUTHORITIES.add(BASIC);
    }

    @Autowired
    OAuthApp.GithubApp githubApp;
    @Autowired
    OAuthApp.AlipayApp alipayApp;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    GithubUserInfoRepository githubUserInfoRepository;
    @Autowired
    AlipayUserInfoRepository alipayUserInfoRepository;
    @Autowired
    OAuthCallbackAddressRepository oAuthCallbackAddressRepository;
    @Autowired
    GithubAccessTokenRepository githubAccessTokenRepository;
    @Autowired
    AlipayAccessTokenRepository alipayAccessTokenRepository;
    @Autowired
    AppRememberMeServices rememberMeServices;

    private final RequestCache requestCache = new HttpSessionRequestCache();

    private static final String TEMPLATE_OAUTH_ACCESS_TOKEN_URI_GITHUB =
            "https://github.com/login/oauth/access_token?client_id=%s&client_secret=%s&code=%s";
    private static final String TEMPLATE_OAUTH_USER_INFO_URI_GITHUB =
            "https://api.github.com/user";

    @GetMapping("github")
    public String github(String code, String state, boolean redirect, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("code=>{}, state=>{}", code, state);

        // 根据请求oauth时的referer进行重定向
        if (!redirect && oAuthCallbackAddressRepository.existsByEncryptSessionIdAndIsDeletedFalse(state)) {
            OAuthCallbackAddress callbackAddress = oAuthCallbackAddressRepository.getByEncryptSessionIdAndIsDeletedFalse(state);
            log.info("callbackAddress code=>{}, state=>{}, referer=>{}", code, state, callbackAddress);
            response.sendRedirect(String.format("%s/%s?%s&redirect=true",
                    UrlUtil.getOrigin(callbackAddress.getReferer()), request.getServletPath(), request.getQueryString()));
            return null;
        }

        /* 获取 access token */
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        GithubAccessToken githubAccessToken =
                JSON.parseObject(okHttpClient.newCall(new Request.Builder()
                                .url(String.format(TEMPLATE_OAUTH_ACCESS_TOKEN_URI_GITHUB,
                                        githubApp.getClientId(),
                                        githubApp.getClientSecret(),
                                        code))
                                .addHeader("accept", "application/json")
                                .build())
                        .execute().body().string(), GithubAccessToken.class);
        log.info("githubAccessToken=>{}", githubAccessToken);

        if (null != githubAccessToken && null != githubAccessToken.getAccess_token()) {
            /* 保存 access token */
            githubAccessTokenRepository.save(githubAccessToken);
            /* 获取 user info */
            GithubUserInfo githubUserInfo =
                    JSON.parseObject(okHttpClient.newCall(new Request.Builder()
                                    .url(TEMPLATE_OAUTH_USER_INFO_URI_GITHUB)
                                    .addHeader("accept", "application/json")
                                    // https://developer.github.com/changes/2020-02-10-deprecating-auth-through-query-param/
                                    .addHeader("Authorization", "token " + githubAccessToken.getAccess_token())
                                    .build())
                            .execute().body().string(), GithubUserInfo.class);
            if (null != githubUserInfo && null != githubUserInfo.getId()) {
                // 保存用户信息
                githubUserInfo.setToken(githubAccessToken);
                githubUserInfoRepository.save(githubUserInfo);
                log.info("githubUserInfo=>{}", githubUserInfo);

                /* 手动设置登录状态 jaas, java认证授权服务 */
                JaasAuthenticationToken jaasAuthenticationToken =
                        new JaasAuthenticationToken(githubUserInfo, githubAccessToken, Collections.EMPTY_LIST, null);
                SecurityContextHolder.getContext().setAuthentication(jaasAuthenticationToken);
                // 记住登录状态
                rememberMeServices.onLoginSuccess(request, response, jaasAuthenticationToken);

                // 尝试重定向到登录前被拦截的请求
                // 重定向到 auth server 授权页面
                // 授权完会重定向到 auth client 登录页面
                // auth client 也记录了登录前被拦截的页面, 最终定向到拦截前的页面
                SavedRequest savedRequest = requestCache.getRequest(request, response);
                String url;
                if (null != savedRequest && null != (url = savedRequest.getRedirectUrl())) {
                    log.info("auth server redirect to url=>{}", url);
                    response.sendRedirect(url);
                    return null;
                }
            }
        }
        if (oAuthCallbackAddressRepository.existsByEncryptSessionIdAndIsDeletedFalse(state)) {
            OAuthCallbackAddress callbackAddress = oAuthCallbackAddressRepository.getByEncryptSessionIdAndIsDeletedFalse(state);
            String referer = callbackAddress.getReferer();
            if (null != referer) {
                response.sendRedirect(String.format("%s#/auth-web/oauth/callbackPage", referer));
                oAuthCallbackAddressRepository.delete(callbackAddress);
                return null;
            }
        }
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("alipay")
    public String alipay(String app_id, String scope, String auth_code, String state, boolean redirect, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("alipay callback, app_id={}, scope={}, auth_code={}, state={}", app_id, scope, auth_code, state);

        // 根据请求oauth时的referer进行重定向
        if (!redirect && oAuthCallbackAddressRepository.existsByEncryptSessionIdAndIsDeletedFalse(state)) {
            OAuthCallbackAddress callbackAddress = oAuthCallbackAddressRepository.getByEncryptSessionIdAndIsDeletedFalse(state);
            log.info("alipay callback, app_id={}, scope={}, auth_code={}, state={}", app_id, scope, auth_code, state);
            log.info("callbackAddress app_id={}, scope={}, auth_code={}, state={}, referer=>{}",
                    app_id, scope, auth_code, state, callbackAddress);
            response.sendRedirect(String.format("%s/oauth/callback/alipay/?%s&redirect=true",
                    UrlUtil.getOrigin(callbackAddress.getReferer()), request.getQueryString()));
            return null;
        }

        /* 获取 access token */
        AlipayClient alipayClient = new DefaultAlipayClient(
                alipayApp.getAlipayGateway(),
                alipayApp.getAppId(),
                alipayApp.getPrivateKey(),
                alipayApp.getFormat(),
                alipayApp.getCharset(),
                alipayApp.getAlipayPublicKey(),
                alipayApp.getSignType());
        AlipaySystemOauthTokenRequest alipaySystemOauthTokenRequest = new AlipaySystemOauthTokenRequest();
        alipaySystemOauthTokenRequest.setGrantType("authorization_code");
        alipaySystemOauthTokenRequest.setCode(auth_code);
        try {
            AlipaySystemOauthTokenResponse alipayAccessToken = alipayClient.execute(alipaySystemOauthTokenRequest);
            if (null != alipayAccessToken && alipayAccessToken.isSuccess()) {
                log.info("alipayAccessToken=>{}", alipayAccessToken);
                AlipayAccessToken accessToken = new AlipayAccessToken();
                BeanUtils.copyProperties(alipayAccessToken, accessToken);
                // 保存 access token
                alipayAccessTokenRepository.save(accessToken);

                AlipayUserInfoShareResponse alipayUserInfo = alipayClient.execute(new AlipayUserInfoShareRequest(), alipayAccessToken.getAccessToken());
                if (null != alipayUserInfo && alipayUserInfo.isSuccess()) {
                    AlipayUserInfo userInfo = new AlipayUserInfo();
                    BeanUtils.copyProperties(alipayUserInfo, userInfo);
                    // 保存用户信息
                    userInfo.setToken(accessToken);
                    alipayUserInfoRepository.save(userInfo);
                    log.info("userInfo=>{}", userInfo);

                    /* 手动设置登录状态 jaas */
                    JaasAuthenticationToken jaasAuthenticationToken =
                            new JaasAuthenticationToken(userInfo, alipayAccessToken, Collections.EMPTY_LIST, null);
                    SecurityContextHolder.getContext().setAuthentication(jaasAuthenticationToken);
                    // 记住登录状态
                    rememberMeServices.onLoginSuccess(request, response, jaasAuthenticationToken);

                    // 尝试重定向到登录前被拦截的请求
                    SavedRequest savedRequest = requestCache.getRequest(request, response);
                    String url;
                    if (null != savedRequest && null != (url = savedRequest.getRedirectUrl())) {
                        log.info("auth server redirect to url=>{}", url);
                        response.sendRedirect(url);
                        return null;
                    }
                }
            }
        } catch (AlipayApiException e) {
            log.info("auth_code={}, 换取userId失败, e.message={}, e.getErrMsg={}", auth_code, e.getMessage(), e.getErrMsg());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (oAuthCallbackAddressRepository.existsByEncryptSessionIdAndIsDeletedFalse(state)) {
            OAuthCallbackAddress callbackAddress = oAuthCallbackAddressRepository.getByEncryptSessionIdAndIsDeletedFalse(state);
            String referer = callbackAddress.getReferer();
            if (referer.endsWith("auth-web")) {
                response.sendRedirect(String.format("%s/#/auth-web/oauth/callbackPage", referer));
                return null;
            }
            if (referer.endsWith("auth-web/")) {
                response.sendRedirect(String.format("%s#/auth-web/oauth/callbackPage", referer));
                return null;
            }
        }
        // 返回当前用户名
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}

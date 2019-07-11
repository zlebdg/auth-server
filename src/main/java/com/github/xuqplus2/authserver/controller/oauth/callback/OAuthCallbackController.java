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
import com.github.xuqplus2.authserver.controller.oauth.token.GithubAccessToken;
import com.github.xuqplus2.authserver.domain.AlipayUserInfo;
import com.github.xuqplus2.authserver.domain.GithubUserInfo;
import com.github.xuqplus2.authserver.repository.AlipayUserInfoRepository;
import com.github.xuqplus2.authserver.repository.GithubUserInfoRepository;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("oauth/callback")
@Slf4j
public class OAuthCallbackController {

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

  private static final String TEMPLATE_OAUTH_ACCESS_TOKEN_URI_GITHUB =
          "https://github.com/login/oauth/access_token?client_id=%s&client_secret=%s&code=%s";
  private static final String TEMPLATE_OAUTH_USER_INFO_URI_GITHUB =
          "https://api.github.com/user?access_token=%s";

  @GetMapping("github")
  public String github(String code, String state, HttpServletRequest request) throws IOException {
    log.info("code=>{}, state=>{}", code, state);
    OkHttpClient okHttpClient = new OkHttpClient();

    /* 获取 access token */
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
      /* 获取 user info */
      GithubUserInfo githubUserInfo =
              JSON.parseObject(okHttpClient.newCall(new Request.Builder()
                      .url(String.format(
                              TEMPLATE_OAUTH_USER_INFO_URI_GITHUB,
                              githubAccessToken.getAccess_token()))
                      .addHeader("accept", "application/json")
                      .build())
                      .execute().body().string(), GithubUserInfo.class);
      if (null != githubUserInfo && null != githubUserInfo.getId()) {
        // 保存用户信息
        githubUserInfoRepository.save(githubUserInfo);
        log.info("githubUserInfo=>{}", githubUserInfo);

        /* 手动设置登录状态 jaas */
        // credential 保存access token,
        // principal 只保存用户id 节省空间, 需要详细信息时, 查询数据库或者用token调用用户信息接口
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("aaa"));
        authorities.add(new SimpleGrantedAuthority("ROLE_admin"));

        String principal = String.format("%s,%s", OAuthApp.GithubApp.class.getSimpleName(), githubUserInfo.getLogin());
        JaasAuthenticationToken jaasAuthenticationToken =
                new JaasAuthenticationToken(principal, githubAccessToken, authorities, null);
        SecurityContextHolder.getContext().setAuthentication(jaasAuthenticationToken);
      }
    }
    // 返回当前用户名
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }

  @GetMapping("alipay")
  public String alipay(String app_id, String scope, String auth_code, String state) {
    log.info("alipay callback, app_id={}, scope={}, auth_code={}, state={}", app_id, scope, auth_code, state);
    AlipayClient alipayClient = new DefaultAlipayClient(
            alipayApp.getAlipayGateway(),
            alipayApp.getAppId(),
            alipayApp.getPrivateKey(),
            alipayApp.getFormat(),
            alipayApp.getCharset(),
            alipayApp.getAlipayPublicKey(),
            alipayApp.getSignType());
    AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
    request.setGrantType("authorization_code");
    request.setCode(auth_code);
    try {
      AlipaySystemOauthTokenResponse alipayAccessToken = alipayClient.execute(request);
      if (null != alipayAccessToken && alipayAccessToken.isSuccess()) {
        log.info("alipayAccessToken=>{}", alipayAccessToken);

        AlipayUserInfoShareResponse alipayUserInfo = alipayClient.execute(new AlipayUserInfoShareRequest(), alipayAccessToken.getAccessToken());
        if (null != alipayUserInfo && alipayUserInfo.isSuccess()) {
          AlipayUserInfo alipayUserInfoLocal = new AlipayUserInfo();
          BeanUtils.copyProperties(alipayUserInfo, alipayUserInfoLocal);
          // 保存用户信息
          alipayUserInfoRepository.save(alipayUserInfoLocal);
          log.info("alipayUserInfoLocal=>{}", alipayUserInfoLocal);

          /* 手动设置登录状态 jaas */
          // credential 保存access token,
          // principal 只保存用户id 节省空间, 需要详细信息时, 查询数据库或者用token调用用户信息接口
          String principal = String.format("%s,%s", OAuthApp.AlipayApp.class.getSimpleName(), alipayUserInfoLocal.getUserId());
          JaasAuthenticationToken jaasAuthenticationToken =
                  new JaasAuthenticationToken(principal, alipayAccessToken, Collections.EMPTY_LIST, null);
          SecurityContextHolder.getContext().setAuthentication(jaasAuthenticationToken);
        }
      }
    } catch (AlipayApiException e) {
      log.info("auth_code={}, 换取userId失败, e.message={}, e.getErrMsg={}", auth_code, e.getMessage(), e.getErrMsg());
    }
    // 返回当前用户名
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}

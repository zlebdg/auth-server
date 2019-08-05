package com.github.xuqplus2.authserver.controller.oauth.login;

import com.github.xuqplus2.authserver.config.OAuthApp;
import com.github.xuqplus2.authserver.domain.oauth.OAuthCallbackAddress;
import com.github.xuqplus2.authserver.repository.OAuthCallbackAddressRepository;
import com.github.xuqplus2.authserver.service.EncryptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
@RequestMapping("oauth/login")
@Slf4j
public class OAuthLoginController {

    private static final String TEMPLATE_AUTHORIZE_URL_GITHUB =
            "redirect:https://github.com/login/oauth/authorize?client_id=%s&redirect_uri=%s&scope=%s&state=%s";
    private static final String TEMPLATE_AUTHORIZE_URL_ALIAPY =
            "redirect:https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=%s&redirect_uri=%s&scope=%s&state=%s";
    final int STATE_LENGTH = 20;

    @Autowired
    OAuthApp.GithubApp githubApp;
    @Autowired
    OAuthApp.AlipayApp alipayApp;
    @Autowired
    EncryptService encryptService;
    @Autowired
    OAuthCallbackAddressRepository callbackAddressRepository;

    @GetMapping("github")
    public ModelAndView github(HttpServletRequest request, ModelAndView mav) {
        String referer = request.getHeader("Referer");
        String sessionId = request.getSession().getId();
        log.info("github, referer={}, sessionId={}", referer, sessionId);

        String encryptSessionId = encryptService.sha256Md5(sessionId);
        if (callbackAddressRepository.existsByEncryptSessionIdAndIsDeletedFalse(encryptSessionId)) {
            callbackAddressRepository.updateRefererById(referer, System.currentTimeMillis(), encryptSessionId);
        } else {
            OAuthCallbackAddress callbackAddress = new OAuthCallbackAddress(encryptSessionId, referer);
            callbackAddressRepository.save(callbackAddress);
        }

        mav.setViewName(String.format(
                TEMPLATE_AUTHORIZE_URL_GITHUB,
                githubApp.getClientId(),
                githubApp.getRedirectUri(),
                githubApp.getScope(),
                encryptSessionId));
        return mav;
    }

    @GetMapping("alipay")
    public ModelAndView alipay(HttpServletRequest request, ModelAndView mav) throws UnsupportedEncodingException {
        String referer = request.getHeader("Referer");
        String sessionId = request.getSession().getId();
        log.info("github, referer={}, sessionId={}", referer, sessionId);

        String encryptSessionId = encryptService.sha256Md5(sessionId);
        if (callbackAddressRepository.existsByEncryptSessionIdAndIsDeletedFalse(encryptSessionId)) {
            callbackAddressRepository.updateRefererById(referer, System.currentTimeMillis(), encryptSessionId);
        } else {
            OAuthCallbackAddress callbackAddress = new OAuthCallbackAddress(encryptSessionId, referer);
            callbackAddressRepository.save(callbackAddress);
        }

        mav.setViewName(String.format(TEMPLATE_AUTHORIZE_URL_ALIAPY,
                alipayApp.getAppId(), URLEncoder.encode(alipayApp.getAuthCallbackUrl(), alipayApp.getCharset()), alipayApp.getScope(), encryptSessionId));
        return mav;
    }
}

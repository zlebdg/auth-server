package com.github.xuqplus2.authserver.config.kz.oauth;

import com.github.xuqplus2.authserver.vo.resp.BasicResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 同样, 如果Accept: application/json, 期望前端登录失败时, 响应的header里不出现<code>302 Location 重定向</code>
 *
 * @return
 */
@Slf4j
@Component
public class AppAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    ContentNegotiationManager contentNegotiationManager;
    @Autowired
    StringHttpMessageConverter stringHttpMessageConverter;

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
}

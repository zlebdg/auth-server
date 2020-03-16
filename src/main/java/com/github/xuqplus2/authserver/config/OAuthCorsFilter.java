package com.github.xuqplus2.authserver.config;

import com.github.xuqplus2.authserver.util.UrlUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/oauth/**")
public class OAuthCorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String referer = request.getHeader("referer");
        response.setHeader("Access-Control-Allow-Credentials", Boolean.TRUE.toString());
        if (!StringUtils.isEmpty(referer)) {
            referer = UrlUtil.getOrigin(referer);
            response.setHeader("Access-Control-Allow-Origin", referer);
        } else {
            response.setHeader("Access-Control-Allow-Origin", "null");
        }
        chain.doFilter(request, response);
    }
}

package com.github.xuqplus2.authserver.controller.handler;

import com.github.xuqplus2.authserver.vo.resp.BasicResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 全局异常处理
 * <p>
 * 异常处理中抛出的异常不会再次被自己捕获, 而是在 ErrorController 中处理并返回给 client
 */
@Slf4j
@RestControllerAdvice
public class AppExceptionHandler { // 捕获 Controller 内的异常

    @Autowired
    ContentNegotiationManager contentNegotiationManager;

    private ResponseEntity handleJson(HttpServletRequest request, String message) {
        return BasicResp.err(getStatus(request), message, null);
    }

    private ModelAndView handleHtml(HttpServletRequest request, String message) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("vo", handleJson(request, message).getBody());
        mav.setViewName("error");
        return mav;
    }

    private Object handle(String message) throws HttpMediaTypeNotAcceptableException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        List<MediaType> mediaTypes = contentNegotiationManager.resolveMediaTypes(new ServletWebRequest(request));
        if (mediaTypes.contains(MediaType.APPLICATION_JSON)) {
            return handleJson(request, message);
        }
        return handleHtml(request, message);
    }

    /**
     * 抛出该异常, 使默认oauth登录授权流程继续
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = InsufficientAuthenticationException.class)
    public Object handle(InsufficientAuthenticationException e) {
        log.error("e.message={}", e.getMessage());
        throw e;
    }

    /**
     * 特定的异常可以做特殊处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BadCredentialsException.class)
    public Object handle(BadCredentialsException e) {
        log.error("e.message={}", e.getMessage());
        return BasicResp.err(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
    }

    /**
     * 简单起见
     *
     * @param t
     * @return
     * @throws HttpMediaTypeNotAcceptableException
     */
    @ExceptionHandler(value = Throwable.class)
    public Object handle(Throwable t) throws HttpMediaTypeNotAcceptableException {
        log.error("t.message={}", t.getMessage());
        return handle(t.getMessage());
    }

    /**
     * copied from {@link AbstractErrorController}
     */
    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}

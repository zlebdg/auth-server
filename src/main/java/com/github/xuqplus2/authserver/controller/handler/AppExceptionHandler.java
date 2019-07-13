package com.github.xuqplus2.authserver.controller.handler;

import com.github.xuqplus2.authserver.controller.test.TestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 * <p>
 * 异常处理中抛出的异常不会再次被自己捕获, 而是在 ErrorController 中处理并返回给 client
 */
@Slf4j
//@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = TestException.class)
    public ResponseEntity handle(HttpServletRequest request, HttpServletResponse response, TestException e) {
        log.error("e.message={}", e.getMessage());
        /* 异常处理器中不要再抛出异常 */
//        if (true) throw new TestException("此异常不会再次被 @ControllerAdvice 给捕获到");
//        if (true) throw new RuntimeException("此异常不会再次被 @ControllerAdvice 给捕获到");
        return new ResponseEntity("error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity handle(HttpServletRequest request, HttpServletResponse response, RuntimeException e) {
        log.error("e.message={}", e.getMessage());
        throw e;
//        return new ResponseEntity("error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handle(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("e.message={}", e.getMessage());
        return new ResponseEntity("error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity handle(HttpServletRequest request, HttpServletResponse response, Throwable t) {
        log.error("t.message={}", t.getMessage());
        return new ResponseEntity("error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = Error.class)
    public ResponseEntity handle(HttpServletRequest request, HttpServletResponse response, Error e) {
        log.error("e.message={}", e.getMessage());
        return new ResponseEntity("error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

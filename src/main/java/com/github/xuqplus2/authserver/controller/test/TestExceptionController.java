package com.github.xuqplus2.authserver.controller.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth/test/exception")
@Slf4j
public class TestExceptionController {

    @GetMapping("a")
    public void a() {
        if (true) throw new TestException("异常处理测试, a");
    }

    @GetMapping("b")
    public void b() {
        if (true) throw new RuntimeException("异常处理测试, b");
    }

    @GetMapping("c")
    public void c() throws Exception {
        if (true) throw new Exception("异常处理测试, c");
    }

    @GetMapping("d")
    public void d() throws Throwable {
        /* Throwable 会被 RuntimeException.class 捕捉到.. (IllegalStateException)*/
        if (true) throw new Throwable("异常处理测试, d");
    }

    @GetMapping("e")
    public void e() {
        /* Error 会被 Exception.class 捕捉到.. (NestedServletException)*/
        if (true) throw new Error("异常处理测试, e");
    }
}

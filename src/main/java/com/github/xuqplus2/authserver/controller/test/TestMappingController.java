package com.github.xuqplus2.authserver.controller.test;

import com.github.xuqplus2.authserver.util.RandomUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequestMapping("test/mapping")
@RestController
public class TestMappingController {
    @Autowired
    RequestMappingHandlerMapping requestMappingHandlerMapping;

    @GetMapping
    public String a(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (RandomUtil.nextInt(10) > 6) {
            request.getRequestDispatcher("mapping/bbb").forward(request, response);
            return null;
        }
        if (RandomUtil.nextInt(10) > 6) {
            request.getRequestDispatcher("/test/mapping/ccc").forward(request, response);
            return null;
        }
        HandlerMethod handlerMethod = requestMappingHandlerMapping.getHandlerMethods().get((RequestMappingInfo.paths("login")).methods(RequestMethod.POST).build());
        return "ok";
    }

    @GetMapping("bbb")
    public String b(HttpServletRequest request, HttpServletResponse response) {
        return "ok bbb";
    }

    @GetMapping("ccc")
    public String c(HttpServletRequest request, HttpServletResponse response, String param) {
        return "ok ccc, param=" + param;
    }

    @PostMapping("ddd")
    public String d(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (RandomUtil.nextInt(10) > 6) {
            request.getRequestDispatcher("/test/mapping/eee").forward(request, response);
            return null;
        }
        return "ok ddd, a=";
    }

    @PostMapping("eee")
    public String e(HttpServletRequest request, HttpServletResponse response, A a) {
        return "ok eee, a=" + a;
    }

    @Data
    public static class A {
        private Long id;
        private String name;
    }

}

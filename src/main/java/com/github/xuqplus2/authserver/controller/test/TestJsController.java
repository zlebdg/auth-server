package com.github.xuqplus2.authserver.controller.test;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth/test/js")
@Slf4j
public class TestJsController {

  @GetMapping("a.js")
  public void a(HttpServletResponse response) throws IOException {
//    response.sendRedirect("http://px6vfmups.bkt.clouddn.com/q.js");
    if (System.currentTimeMillis() % 2 == 0) {
      response.sendRedirect("/test/js/c.js");
    } else {
      response.sendRedirect("/test/js/b.js");
    }
  }

  @GetMapping(value = "b.js", produces = "application/javascript")
  public String b(HttpServletResponse response) throws IOException {
    return "console.log(1)";
  }

  @GetMapping("c.js")
  public void c(HttpServletResponse response) throws IOException {
    response.setContentType("text/javascript");
    response.getWriter().write("console.log(2)");
  }
}

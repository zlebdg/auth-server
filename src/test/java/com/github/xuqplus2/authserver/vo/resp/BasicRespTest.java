package com.github.xuqplus2.authserver.vo.resp;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Slf4j
@Ignore
public class BasicRespTest {

    @Test
    public void ok() {
        String a = "";

        Assert.assertTrue(a instanceof String);
        Assert.assertTrue(a instanceof Serializable);
    }

    @Test
    public void b() {
        for (int i = 0; i < 1000; i++) {
            try {
                HttpStatus status = HttpStatus.valueOf(i);
                log.info("status=>{}", status);
            } catch (Exception e) {

            }
        }
    }

    @Test
    public void c() {
        for (int i = 0; i < 1000; i++) {
            try {
                HttpStatus status = HttpStatus.valueOf(i);
                log.info("{}, status=>{}", status.value() / 100, status);
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
    }

    @Test
    public void d() {
        HttpStatus status = HttpStatus.valueOf(null);
    }
}
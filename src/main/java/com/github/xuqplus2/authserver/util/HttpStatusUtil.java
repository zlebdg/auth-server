package com.github.xuqplus2.authserver.util;

import org.springframework.http.HttpStatus;

public class HttpStatusUtil {

    public static final HttpStatus valueOf(Integer code) {
        try {
            return HttpStatus.valueOf(code);
        } catch (IllegalArgumentException e) {
            return HttpStatus.valueOf(code / 100 * 100);
        }
    }
}

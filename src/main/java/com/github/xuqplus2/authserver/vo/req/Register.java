package com.github.xuqplus2.authserver.vo.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Register {
    @NotBlank
    private String username;
    @NotBlank
    private String email;
    @NotBlank
    private String captcha;
}

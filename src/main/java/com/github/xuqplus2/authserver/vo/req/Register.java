package com.github.xuqplus2.authserver.vo.req;

import com.github.xuqplus2.authserver.vo.VO;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class Register extends VO {

    @NotBlank
    @Pattern(regexp = "^[\\w\\u4e00-\\u9fa5]{1,64}$")
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String captcha;
    private String verifyUri;
}

package com.github.xuqplus2.authserver.vo.req.auth.register;

import com.github.xuqplus2.authserver.vo.VO;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class Register extends VO {

    public static final String USERNAME_REGEXP = "^[\\w\\u4e00-\\u9fa5-#./\\\\]{1,64}$";

    @NotBlank
    @Pattern(regexp = USERNAME_REGEXP, message = "用户名为中文英文数字和下划线, 不能含有特殊符号")
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String captcha;
    private String verifyUri;
}

package com.github.xuqplus2.authserver.vo.req.auth.reset;

import com.github.xuqplus2.authserver.vo.VO;
import com.github.xuqplus2.authserver.vo.req.auth.register.Register;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class PasswordResetVerify extends VO {

    @NotBlank
    @Pattern(regexp = Register.USERNAME_REGEXP)
    private String username;
    @NotBlank
    private String verifyCode;
    private String password;
}

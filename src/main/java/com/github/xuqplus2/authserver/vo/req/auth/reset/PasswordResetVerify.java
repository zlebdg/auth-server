package com.github.xuqplus2.authserver.vo.req.auth.reset;

import com.github.xuqplus2.authserver.vo.VO;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PasswordResetVerify extends VO {

    @NotBlank
    private String verifyCode;
    private String password;
}

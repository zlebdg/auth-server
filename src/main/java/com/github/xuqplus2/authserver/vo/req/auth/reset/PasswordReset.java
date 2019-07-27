package com.github.xuqplus2.authserver.vo.req.auth.reset;

import com.github.xuqplus2.authserver.vo.VO;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PasswordReset extends VO {

    private String username;
    private String email;
    @NotBlank
    private String captcha;
    private String verifyUri;
}

package com.github.xuqplus2.authserver.vo.req.auth.register;

import com.github.xuqplus2.authserver.vo.VO;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.github.xuqplus2.authserver.vo.req.auth.register.Register.USERNAME_REGEXP;

@Data
public class ResendEmail extends VO {

    @NotBlank
    @Pattern(regexp = USERNAME_REGEXP)
    private String username;
    @NotBlank
    @Email
    private String email;
}

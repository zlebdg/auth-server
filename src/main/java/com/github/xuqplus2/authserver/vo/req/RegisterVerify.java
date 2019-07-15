package com.github.xuqplus2.authserver.vo.req;

import com.github.xuqplus2.authserver.vo.VO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegisterVerify extends VO {

    @NotBlank
    @Pattern(regexp = Register.USERNAME_REGEXP)
    private String username;
    @NotBlank
    private String verifyCode;
    private String password;
}

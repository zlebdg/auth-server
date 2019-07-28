package com.github.xuqplus2.authserver.vo.req.auth.register;

import com.github.xuqplus2.authserver.vo.VO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegisterVerify extends VO {

    @NotBlank
    private String verifyCode;
    private String password;
}

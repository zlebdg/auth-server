package com.github.xuqplus2.authserver.vo.req;

import com.github.xuqplus2.authserver.vo.VO;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.validation.constraints.NotBlank;

@Data
public class Login extends VO {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public Authentication getAuthentication() {
        return new UsernamePasswordAuthenticationToken(this.username, this.password);
    }
}

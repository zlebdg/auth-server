package com.github.xuqplus2.authserver.domain;

import com.github.xuqplus2.authserver.vo.req.RegisterVerify;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppUser extends BasicDomain {

    @Id
    @Column(length = 64)
    String username;
    @Column(nullable = false, unique = true, length = 255)
    String email;
    @Column(length = 16)
    String salt;
    @Column(nullable = false)
    String password;

    public AppUser(AppRegister register, RegisterVerify verify) {
        this.username = register.getUsername();
        this.email = register.getEmail();
        this.password = register.getPassword();
        if (StringUtils.isEmpty(this.password)) {
            this.password = verify.getPassword();
        }
    }
}

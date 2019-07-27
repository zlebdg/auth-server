package com.github.xuqplus2.authserver.domain;

import com.github.xuqplus2.authserver.util.RandomUtil;
import com.github.xuqplus2.authserver.vo.req.auth.reset.PasswordReset;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppPasswordReset extends BasicDomain {

    private static final int VERIFY_CODE_LENGTH = 64;

    @Id
    @Column(length = 64)
    String username;
    @Column(nullable = false, unique = true, length = 255)
    String email;
    String verifyCode;
    String verifyUri;

    public AppPasswordReset(PasswordReset reset) {
        this.username = reset.getUsername();
        this.email = reset.getEmail();
        this.verifyCode = RandomUtil.string(VERIFY_CODE_LENGTH);
        this.verifyUri = reset.getVerifyUri();
    }

    public void refreshVerifyCode() {
        this.verifyCode = RandomUtil.string(VERIFY_CODE_LENGTH);
    }
}


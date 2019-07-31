package com.github.xuqplus2.authserver.domain;

import com.github.xuqplus2.authserver.util.RandomUtil;
import com.github.xuqplus2.authserver.vo.req.auth.register.Register;
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
public class AppRegister extends BasicDomain {

    // 注册验证code长度
    private static final int VERIFY_CODE_LENGTH = 64;
    // 注册信息过期时间
    public static final long EXPIRED_TIME_MILLS = 1000L * 60 * 60 * 24;

    @Id
    @Column(length = 64)
    String username;
    @Column(nullable = false, unique = true, length = 255)
    String email;
    String verifyCode;
    @Column(length = 16)
    String salt;
    String password;
    String verifyUri;

    public AppRegister(Register register) {
        this.username = register.getUsername();
        this.email = register.getEmail();
        this.verifyCode = RandomUtil.string(VERIFY_CODE_LENGTH);
        this.verifyUri = register.getVerifyUri();
    }

    public void refreshVerifyCode() {
        this.verifyCode = RandomUtil.string(VERIFY_CODE_LENGTH);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - this.getCreateAt() > EXPIRED_TIME_MILLS;
    }
}


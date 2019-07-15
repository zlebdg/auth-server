package com.github.xuqplus2.authserver.domain;

import com.github.xuqplus2.authserver.service.EncryptService;
import com.github.xuqplus2.authserver.util.AuthorityUtil;
import com.github.xuqplus2.authserver.util.RandomUtil;
import com.github.xuqplus2.authserver.vo.req.RegisterVerify;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppUser extends BasicDomain implements UserDetails {

    private static final int PASSWORD_SALT_LENGTH = 16;

    @Id
    @Column(length = 64)
    String username;
    @Column(nullable = false, unique = true, length = 255)
    String email;
    @Column(length = 16)
    String salt;
    @Column(nullable = false)
    String password;
    /* cascade 级联操作权限 */
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    public Set<AppRole> appRoles;
    /* fetch 关联加载, 延迟/立即 */
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    public Set<AppAuthority> appAuthorities;

    public AppUser(AppRegister register, RegisterVerify verify) {
        this.username = register.getUsername();
        this.email = register.getEmail();
        if (StringUtils.isEmpty(this.password)) {
            this.password = verify.getPassword();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new LinkedHashSet<>();
        AuthorityUtil.addAuthorities(grantedAuthorities, this);
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /* 加密并设置密码 */
    public void setNewPassword(String password, EncryptService encryptService) {
        this.salt = RandomUtil.string(PASSWORD_SALT_LENGTH);
        this.password = encryptService.encodeAppUserPassword(this.salt + password);
    }

    /* 检查密码 */
    public void checkPassword(Object password, EncryptService encryptService) {
        if (null == this.salt || null == this.password
                || !this.password.equals(encryptService.encodeAppUserPassword(this.salt + password))) {
            throw new AuthenticationServiceException("密码不正确");
//            throw new BadCredentialsException("密码不正确");
        }
    }
}

package com.github.xuqplus2.authserver.domain;

import com.github.xuqplus2.authserver.config.kz.RememberMeInfo;
import com.github.xuqplus2.authserver.service.EncryptService;
import com.github.xuqplus2.authserver.util.AuthorityUtil;
import com.github.xuqplus2.authserver.util.RandomUtil;
import com.github.xuqplus2.authserver.vo.req.auth.register.RegisterVerify;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.*;

import static com.github.xuqplus2.authserver.domain.AppAuthorities.ARTICLE;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppUser extends BasicDomain implements UserDetails, RememberMeInfo {

    public static final List<GrantedAuthority> APP_USER_DEFAULT_AUTHORITIES = Collections.singletonList(ARTICLE.getAuthority());

    private static final int PASSWORD_SALT_LENGTH = 16;
    public static final String DEFAULT_PASSWORD_ENCRYPT = "app"; // 密码加密方式

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
    // todo, 不知道怎么给spring security rememberMeFilter加上 @Transactional
    // 只有改成 FetchType.EAGER
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    public Set<AppRole> appRoles;
    /* fetch 关联加载, 延迟/立即 */
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
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
        grantedAuthorities.addAll(APP_USER_DEFAULT_AUTHORITIES);
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
        this.password = encryptService.encryptAppUserPassword(password);
    }

    @Override
    public String getRememberName() {
        return this.username;
    }
}

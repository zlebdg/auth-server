package com.github.xuqplus2.authserver.domain.oauth;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.xuqplus2.authserver.config.OAuthApp;
import com.github.xuqplus2.authserver.config.kz.RememberMeInfo;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.github.xuqplus2.authserver.domain.AppAuthorities.ARTICLE;

@Data
@Entity
public class AlipayUserInfo implements UserDetails, RememberMeInfo {

    public static final List<GrantedAuthority> ALIPAY_USER_DEFAULT_AUTHORITIES = Collections.singletonList(ARTICLE.getAuthority());

    private String code;
    private String msg;
    private String avatar; // 头像uri
    private String city;
    private String gender;
    private String isCertified;
    private String isStudentCertified;
    private String nickName;
    private String province;
    @Id
    private String userId;
    private String userStatus;
    private String userType;

    @ManyToOne
    @ToString.Exclude
    @JSONField(serialize = false)
    private AlipayAccessToken token;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return ALIPAY_USER_DEFAULT_AUTHORITIES;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userId;
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

    @Override
    public final String getRememberName() {
        return String.format("%s,%s", OAuthApp.AlipayApp.class.getSimpleName(), this.userId);
    }

    @Override
    public final void setUsername(String username) {
        this.userId = username;
    }
}

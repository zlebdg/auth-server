package com.github.xuqplus2.authserver.domain.oauth;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Collection;
import java.util.Collections;

@Data
@Entity
public class AlipayUserInfo implements UserDetails {
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
        return Collections.EMPTY_SET;
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
}

package com.github.xuqplus2.authserver.util;

import com.github.xuqplus2.authserver.domain.AppAuthority;
import com.github.xuqplus2.authserver.domain.AppRole;
import com.github.xuqplus2.authserver.domain.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

public class AuthorityUtil {

    public static final void addAuthorities(Set<GrantedAuthority> authorities, AppUser user) {
        if (null == authorities) return;
        if (null == user) return;

        if (null != user.getAppRoles()) {
            user.getAppRoles().stream().forEach(role -> addAuthorities(authorities, role));
        }
        if (null != user.getAppAuthorities()) {
            user.getAppAuthorities().stream().forEach(authority -> addAuthorities(authorities, authority));
        }
    }

    private static final void addAuthorities(Set<GrantedAuthority> authorities, AppRole role) {
        authorities.add(new SimpleGrantedAuthority(role.getRole())); // role缺省的权限
        if (null != role.getAuthorities()) { // role对应的权限
            for (AppAuthority authority : role.getAuthorities()) {
                addAuthorities(authorities, authority);
            }
        }
    }

    private static final void addAuthorities(Set<GrantedAuthority> authorities, AppAuthority authority) {
        authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
    }
}

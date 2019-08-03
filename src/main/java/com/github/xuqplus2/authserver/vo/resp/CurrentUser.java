package com.github.xuqplus2.authserver.vo.resp;

import com.github.xuqplus2.authserver.domain.AppUser;
import com.github.xuqplus2.authserver.vo.VO;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Data
public class CurrentUser extends VO {

    private String username;
    private String appId;
    private Boolean authenticated;
    private Collection<String> authorities = new LinkedHashSet();
    private Collection<String> roles = new LinkedHashSet();

    public CurrentUser() {
    }

    public CurrentUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof AppUser) {
            AppUser appUser = (AppUser) principal;
            this.username = appUser.getUsername();
            this.authenticated = authentication.isAuthenticated();
            this.authorities.addAll(appUser.getAuthorities().stream().map(authority -> {
                return authority.getAuthority();
            }).collect(Collectors.toSet()));
            this.roles.addAll(appUser.getAppRoles().stream().map(role -> {
                return role.getRole();
            }).collect(Collectors.toSet()));
        } else if (principal instanceof UserDetails) {
            UserDetails user = (UserDetails) principal;
            this.username = user.getUsername();
            this.authenticated = authentication.isAuthenticated();
            this.authorities.addAll(user.getAuthorities().stream().map(authority -> {
                return authority.getAuthority();
            }).collect(Collectors.toSet()));
        } else if (principal instanceof String) {
            this.username = (String) principal;
        }
    }
}

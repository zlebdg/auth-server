package com.github.xuqplus2.authserver.vo.resp;

import com.github.xuqplus2.authserver.domain.AppUser;
import com.github.xuqplus2.authserver.vo.VO;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Data
public class CurrentUser extends VO {

    private String username;
    private String appId;
    private Boolean authenticated;
    private Collection authorities;
    private Collection appRoles;
    private Collection appAuthorities;

    public CurrentUser() {
    }

    public CurrentUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof AppUser) {
            AppUser appUser = (AppUser) principal;
            this.username = appUser.getUsername();
            this.authenticated = authentication.isAuthenticated();
            this.authorities = appUser.getAuthorities();
            this.appRoles = appUser.getAppRoles();
            this.appAuthorities = appUser.getAppAuthorities();
        } else if (principal instanceof User) {
            User user = (User) principal;
            this.username = user.getUsername();
            this.authenticated = authentication.isAuthenticated();
            this.authorities = user.getAuthorities();
        }
    }
}

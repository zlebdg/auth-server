package com.github.xuqplus2.authserver.vo.resp;

import com.github.xuqplus2.authserver.domain.AppUser;
import com.github.xuqplus2.authserver.domain.oauth.AlipayUserInfo;
import com.github.xuqplus2.authserver.domain.oauth.GithubUserInfo;
import com.github.xuqplus2.authserver.vo.VO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class CurrentUser extends VO {

    private String username; // 唯一的id
    private String nickname; // 显示的名字
    private String avatar;   // 头像地址
    private String appId;
    private Boolean authenticated;
    private Collection<String> authorities = new LinkedHashSet();
    private Collection<String> roles = new LinkedHashSet();

    public CurrentUser() {
    }

    public CurrentUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof AppUser) {
            AppUser user = (AppUser) principal;
            this.username = user.getUsername();
            this.nickname = user.getUsername();
            this.authenticated = authentication.isAuthenticated();
            this.authorities.addAll(user.getAuthorities().stream().map(authority -> {
                return authority.getAuthority();
            }).collect(Collectors.toSet()));
            this.roles.addAll(user.getAppRoles().stream().map(role -> {
                return role.getRole();
            }).collect(Collectors.toSet()));
        } else if (principal instanceof GithubUserInfo) {
            GithubUserInfo user = (GithubUserInfo) principal;
            this.username = user.getUsername();
            this.nickname = user.getUsername();
            this.avatar = user.getAvatar_url();
            this.authenticated = authentication.isAuthenticated();
            this.authorities.addAll(user.getAuthorities().stream().map(authority -> {
                return authority.getAuthority();
            }).collect(Collectors.toSet()));

        } else if (principal instanceof AlipayUserInfo) {
            AlipayUserInfo user = (AlipayUserInfo) principal;
            this.username = user.getUsername();
            this.nickname = user.getNickName();
            this.avatar = user.getAvatar();
            this.authenticated = authentication.isAuthenticated();
            this.authorities.addAll(user.getAuthorities().stream().map(authority -> {
                return authority.getAuthority();
            }).collect(Collectors.toSet()));
        } else if (principal instanceof UserDetails) {
            UserDetails user = (UserDetails) principal;
            this.username = user.getUsername();
            this.authenticated = authentication.isAuthenticated();
            this.authorities.addAll(user.getAuthorities().stream().map(authority -> {
                return authority.getAuthority();
            }).collect(Collectors.toSet()));
            if (principal instanceof AlipayUserInfo) {
                this.nickname = ((AlipayUserInfo) principal).getNickName();
            } else {
                this.nickname = this.username;
            }
        } else if (principal instanceof String) {
            this.username = (String) principal;
        }
    }
}

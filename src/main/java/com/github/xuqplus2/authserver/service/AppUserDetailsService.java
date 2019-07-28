package com.github.xuqplus2.authserver.service;

import com.github.xuqplus2.authserver.domain.AppUser;
import com.github.xuqplus2.authserver.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = username.contains("@")
                ? appUserRepository.getByEmail(username)
                : appUserRepository.getByUsername(username);
        if (null == appUser) {
            throw new UsernameNotFoundException("账号不存在");
        }
        return appUser;
    }
}

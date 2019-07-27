package com.github.xuqplus2.authserver.repository;

import com.github.xuqplus2.authserver.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, String> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    AppUser getByUsername(String username);

    AppUser getByEmail(String email);
}

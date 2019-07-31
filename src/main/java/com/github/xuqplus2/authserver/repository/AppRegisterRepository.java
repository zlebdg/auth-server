package com.github.xuqplus2.authserver.repository;

import com.github.xuqplus2.authserver.domain.AppRegister;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRegisterRepository extends JpaRepository<AppRegister, String> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    AppRegister getByUsername(String username);

    AppRegister getByUsernameAndEmailAndIsDeletedFalse(String username, String email);

    void deleteByCreateAtLessThan(long l);

    void deleteByCreateAtLessThanAndIsDeletedTrue(long l);
}

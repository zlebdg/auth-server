package com.github.xuqplus2.authserver.repository;

import com.github.xuqplus2.authserver.domain.AppPasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppPasswordResetRepository extends JpaRepository<AppPasswordReset, String> {

    AppPasswordReset getByUsername(String username);

    void deleteByCreateAtLessThan(long l);

    void deleteByCreateAtLessThanAndIsDeletedTrue(long l);
}

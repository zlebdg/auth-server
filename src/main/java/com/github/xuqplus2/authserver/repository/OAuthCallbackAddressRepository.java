package com.github.xuqplus2.authserver.repository;

import com.github.xuqplus2.authserver.domain.OAuthCallbackAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface OAuthCallbackAddressRepository extends JpaRepository<OAuthCallbackAddress, String> {

    boolean existsByEncryptSessionIdAndIsDeletedFalse(String id);

    OAuthCallbackAddress getByEncryptSessionIdAndIsDeletedFalse(String id);

    @Transactional
    @Modifying
    @Query("update OAuthCallbackAddress a set a.referer = ?1, a.updateAt = ?2 where a.encryptSessionId = ?3")
    void updateRefererById(String referer, long l, String sessionId);
}

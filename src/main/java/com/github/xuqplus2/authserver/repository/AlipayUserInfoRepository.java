package com.github.xuqplus2.authserver.repository;

import com.github.xuqplus2.authserver.domain.oauth.AlipayUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlipayUserInfoRepository extends JpaRepository<AlipayUserInfo, String> {
    AlipayUserInfo getByUserId(String userId);
}

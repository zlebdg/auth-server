package com.github.xuqplus2.authserver.repository;

import com.github.xuqplus2.authserver.domain.oauth.AlipayAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlipayAccessTokenRepository extends JpaRepository<AlipayAccessToken, Long> {
}

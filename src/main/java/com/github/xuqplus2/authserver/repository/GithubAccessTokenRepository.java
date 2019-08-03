package com.github.xuqplus2.authserver.repository;

import com.github.xuqplus2.authserver.domain.oauth.GithubAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubAccessTokenRepository extends JpaRepository<GithubAccessToken, Long> {
}

package com.github.xuqplus2.authserver.domain.oauth;

import com.github.xuqplus2.authserver.domain.BasicDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AlipayAccessToken extends BasicDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accessToken;
    private String authTokenType;
    private String expiresIn;
    private String reExpiresIn;
    private String refreshToken;
    private String userId;
}

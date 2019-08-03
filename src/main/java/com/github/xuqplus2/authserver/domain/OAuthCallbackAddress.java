package com.github.xuqplus2.authserver.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class OAuthCallbackAddress extends BasicDomain {
    @Id
    private String encryptSessionId; // sessionId散列
    private String referer;

    public OAuthCallbackAddress() {
    }

    public OAuthCallbackAddress(String sessionId, String referer) {
        this.encryptSessionId = sessionId;
        this.referer = referer;
    }
}

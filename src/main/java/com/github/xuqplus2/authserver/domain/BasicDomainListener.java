package com.github.xuqplus2.authserver.domain;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class BasicDomainListener {

    @PrePersist
    public void PrePersist(BasicDomain domain) {
        if (null == domain.createAt) domain.createAt = System.currentTimeMillis();
        if (null == domain.isDeleted) domain.isDeleted = false;
    }

    @PreUpdate
    public void PreUpdate(BasicDomain domain) {
        domain.updateAt = System.currentTimeMillis();
    }
}

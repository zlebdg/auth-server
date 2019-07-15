package com.github.xuqplus2.authserver.domain;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class BasicDomainListener {

    @PrePersist
    public void PrePersist(BasicDomain domain) {
        if (null == domain.getCreateAt()) domain.setCreateAt(System.currentTimeMillis());
        if (null == domain.getIsDeleted()) domain.setIsDeleted(false);
    }

    @PreUpdate
    public void PreUpdate(BasicDomain domain) {
        domain.setUpdateAt(System.currentTimeMillis());
    }
}

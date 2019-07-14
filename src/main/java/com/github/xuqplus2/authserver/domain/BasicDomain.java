package com.github.xuqplus2.authserver.domain;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;

@MappedSuperclass
@EntityListeners(BasicDomainListener.class)
public abstract class BasicDomain implements Serializable {

    @Version
    Long version;
    Long createAt;
    Long updateAt;
    Boolean isDeleted;

    public BasicDomain() {
    }
}

package com.github.xuqplus2.authserver.domain;

import lombok.Data;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;

@Data
@MappedSuperclass
@EntityListeners(BasicDomainListener.class)
public abstract class BasicDomain implements Serializable {

    @Version
    private Long version;
    private Long createAt;
    private Long updateAt;
    private Boolean isDeleted;

    public BasicDomain() {
    }
}

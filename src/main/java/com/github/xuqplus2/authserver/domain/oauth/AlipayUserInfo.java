package com.github.xuqplus2.authserver.domain.oauth;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class AlipayUserInfo implements Serializable {
    private String code;
    private String msg;
    private String avatar; // 头像uri
    private String city;
    private String gender;
    private String isCertified;
    private String isStudentCertified;
    private String nickName;
    private String province;
    @Id
    private String userId;
    private String userStatus;
    private String userType;
}

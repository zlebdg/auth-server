package com.github.xuqplus2.authserver.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class AlipayUserInfo {
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

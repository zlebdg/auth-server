package com.github.xuqplus2.authserver.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class GithubUserInfo {
    @Column(unique = true)
    private String login;
    @Id
    private Long id;
    private String node_id;
    private String type;
    private Boolean site_admin;
    private String name;
    private String company;
    private String blog;
    private String location;
    private String email;
    private String hireable;
    private String bio;
    private Integer public_repos;
    private Integer public_gists;
    private Integer followers;
    private Integer following;
    private Date created_at;
    private Date updated_at;
}

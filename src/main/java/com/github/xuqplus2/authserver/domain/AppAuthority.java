package com.github.xuqplus2.authserver.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@NoArgsConstructor
public class AppAuthority {

    @NotBlank
    @Column(length = 32)
    private String name;
    @Id
    @NotBlank
    @Column(length = 32)
    private String authority;

    public AppAuthority(@NotBlank String name, @NotBlank String authority) {
        this.name = name;
        this.authority = authority;
    }
}

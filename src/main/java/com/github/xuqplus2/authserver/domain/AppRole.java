package com.github.xuqplus2.authserver.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class AppRole {

    @NotBlank
    @Column(length = 32)
    private String name;
    @Id
    @NotBlank
    @Column(length = 32)
    private String role;
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    public Set<AppAuthority> authorities;

    public AppRole(@NotBlank String name, @NotBlank String role) {
        this.name = name;
        this.role = role;
    }
}

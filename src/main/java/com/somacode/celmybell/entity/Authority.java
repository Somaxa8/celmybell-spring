package com.somacode.celmybell.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(of = "role")
@ToString(of = "role")
public class Authority implements Serializable {

    public enum Role {
        ADMIN, SWAGGER
    }

    @Id @Enumerated(EnumType.STRING)
    private Role role;
    private String description;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "rel_user_authority",
            joinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private Set<User> users = new HashSet<>();

    @Transient
    private Boolean enabled;

}

package com.somacode.celmybell.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(of = "name")
@ToString(of = "name")
public class Authority {

    public enum Name {
        ROLE_SUPERADMIN, ROLE_ADMIN, ROLE_USER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Name name;
    private String description;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "rel_user_authority",
            joinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private Set<User> users = new HashSet<>();
}

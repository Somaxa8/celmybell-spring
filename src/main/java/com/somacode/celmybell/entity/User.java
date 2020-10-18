package com.somacode.celmybell.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class User implements Serializable {

    @Id @GeneratedValue
    private Long id;

    private String username;
    private String name;
    private String lastname;
    private String email;

    @JsonIgnore
    private String password;
    private Boolean activated;

    @JsonIgnore
    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();
}

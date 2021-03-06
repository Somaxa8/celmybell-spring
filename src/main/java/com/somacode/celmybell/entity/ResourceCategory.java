package com.somacode.celmybell.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ResourceCategory implements Serializable {

    @Id @GeneratedValue
    private Long id;

    private String title;

    @JsonIgnore
    @OneToMany(mappedBy = "resourceCategory")
    private List<Resource> resources = new ArrayList<>();

    @ManyToOne
    private ResourceCategory parent;

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    private List<ResourceCategory> children = new ArrayList<>();

    @Transient
    private Boolean isParent;

}

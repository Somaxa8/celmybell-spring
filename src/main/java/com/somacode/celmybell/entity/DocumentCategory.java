package com.somacode.celmybell.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class DocumentCategory implements Serializable {

    @Id @GeneratedValue
    private Long id;

    private String category;

    @JsonIgnore
    @OneToMany(mappedBy = "documentCategory")
    private List<Document> documents = new ArrayList<>();

    @ManyToOne
    private DocumentCategory parent;

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    private List<DocumentCategory> children = new ArrayList<>();

    @Transient
    private Boolean isParent;

}

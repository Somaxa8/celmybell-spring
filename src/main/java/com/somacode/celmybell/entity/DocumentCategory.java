package com.somacode.celmybell.entity;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

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

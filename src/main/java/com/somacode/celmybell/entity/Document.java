package com.somacode.celmybell.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.somacode.celmybell.service.tool.StorageSerialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class Document implements Serializable {

    public enum Type {
        IMAGE, DOCUMENT
    }

    @Id @GeneratedValue
    private Long id;

    @JsonSerialize(using = StorageSerialize.class)
    private String url;

    private String name;    // saved file name, ej: 114D112938102938012938102398
    private String baseName; // name of the uploaded file
    private String extension; // Detect file extension
    private String title;
    private String description;
    private String tag;

    @Enumerated(EnumType.STRING)
    private Type type;

}

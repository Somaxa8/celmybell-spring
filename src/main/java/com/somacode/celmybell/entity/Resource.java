package com.somacode.celmybell.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Resource {
    @Id @GeneratedValue
    private Long id;
    private String title;
    private String description;
    @OneToOne
    private Document document;
    @ManyToOne
    private ResourceCategory resourceCategory;
}

package com.somacode.celmybell.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class DrawingCategory implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String category;

    @ManyToMany
    private DrawingSubcategory drawingSubcategory;
}

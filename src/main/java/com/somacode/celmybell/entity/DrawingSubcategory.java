package com.somacode.celmybell.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class DrawingSubcategory implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private String subcategory;
}

package com.somacode.celmybell.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class Drawing implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private String drawing;
    private String title;
    private String description;

    @ManyToOne
    private DrawingCategory drawingCategory;

}

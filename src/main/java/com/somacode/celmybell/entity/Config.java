package com.somacode.celmybell.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class Config implements Serializable {

    @Id @GeneratedValue
    private Long id;
    private String key;
    private String value;
    private String url;
    private String meta;
    private String tag;
}

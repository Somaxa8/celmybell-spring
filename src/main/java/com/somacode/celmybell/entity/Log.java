package com.somacode.celmybell.entity;

import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;

//@Document
@Data
public class Log {

    @Id
    private Long millis;
    private Long user;
    private String type;
    private String error;
    private String content;
    private String className;
    private String methodName;
    private Integer lineNumber;

    private DateTime date;

    private String ip;
    private String methodHttp;
    private String endpoint;
    private Integer executionTime;

}

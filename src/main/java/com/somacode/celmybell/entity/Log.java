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
//    @JsonProperty("class")
//    @Field("class_name")
    private String className;
//    @JsonProperty("methodName")
//    @Field("method_name")
    private String methodName;
//    @JsonProperty("line")
//    @Field("line_number")
    private Integer lineNumber;

    private DateTime date;

    private String ip;
//    @Field("method_http")
    private String methodHttp;
    private String endpoint;
//    @Field("execution_time")
    private Integer executionTime;

}

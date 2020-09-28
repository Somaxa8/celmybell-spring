package com.somacode.celmybell.service;

import com.somacode.celmybell.CelmybellApplication;
import com.somacode.celmybell.entity.Log;
import com.somacode.celmybell.service.tool.SecurityTool;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@Transactional
public class LogService {

    private static final String PACKAGE_NAME = CelmybellApplication.class.getPackage().toString().split(" ")[1];

    public static LogService out;

//    @Autowired LogRepository logRepository;
    @Autowired SecurityTool securityService;


    @PostConstruct
    public void init() {
        out = this;
    }

    public enum Type {
        INFO, WARN, ERROR, DEBUG, ENDPOINT
    }

    public Log info(Exception e) {
        return save(e, null, Type.INFO);
    }

    public Log debug(Exception e) {
        return save(e, null, Type.DEBUG);
    }

    public Log warn(Exception e) {
        return save(e, null, Type.WARN);
    }

    public Log error(Exception e) {
        return save(e, null, Type.ERROR);
    }

    public Log info(String s) {
        return save(null, s, Type.INFO);
    }

    public Log debug(String s) {
        return save(null, s, Type.DEBUG);
    }

    public Log warn(String s) {
        return save(null, s, Type.WARN);
    }

    public Log error(String s) {
        return save(null, s, Type.ERROR);
    }

    public Log endpoint(String s, int executionTime, String methodHttp, String endpoint, String ipAddress) {
        return save(null, s, Type.ENDPOINT, executionTime, methodHttp, endpoint, ipAddress);
    }

    private Log save(Exception e, String content, Type type) {
        return save(e, content, type, null, null, null, null);
    }

    private Log save(Exception e, String content, Type type, Integer executionTime, String methodHttp, String endpoint, String ipAddress) {
        Log log = new Log();
        log.setMillis(System.currentTimeMillis());
        log.setType(type.toString());
        log.setExecutionTime(executionTime);
        log.setMethodHttp(methodHttp);
        log.setEndpoint(endpoint);
        log.setIp(ipAddress);
        log.setDate(DateTime.now());
        if (securityService.isAuthenticated()) {
            log.setUser(securityService.getUserId());
        }
        if (e != null) {
            log.setContent(e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
            log.setError(e.getClass().getSimpleName());
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                if (stackTraceElement.getClassName().contains(PACKAGE_NAME)) {
                    log.setClassName(stackTraceElement.getClassName());
                    log.setMethodName(stackTraceElement.getMethodName());
                    log.setLineNumber(stackTraceElement.getLineNumber());
                    break;
                }
            }
        } if (content != null) {
            log.setContent(content);
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            log.setClassName(stackTraceElement.getClassName());
            log.setMethodName(stackTraceElement.getMethodName());
            log.setLineNumber(stackTraceElement.getLineNumber());
        }

//        logRepository.save(log);
        String typeString = null;
        switch (type) {
            case INFO: typeString = "[21;32m" + "  INFO"; break;
            case ERROR: typeString = "[21;31m" + " ERROR"; break;
            case WARN: typeString = "[21;33m" + "  WARN"; break;
            case DEBUG: typeString = "[21;32m" + " DEBUG"; break;
            case ENDPOINT: typeString = "[21;34m" + " ENDPOINT"; break;
        }

        String[] completeClass = log.getClassName().split("\\.");
        String className = completeClass[completeClass.length - 1];

        if (className.contains("$")) className = className.split("\\$")[0];

        System.out.println(DateTime.now().toString("yyyy-MM-dd HH:mm:ss.SSS") +
                (char)27 + typeString + (char)27 + "[0m" +
                " ___ [LogService] " +
                "(" + className + ".java:" + log.getLineNumber() + ") " +
                (log.getError() != null ? (char)27 + "[21;36m" + log.getError() + ": " + (char)27 + "[0m" : "") + log.getContent());

        return log;
    }

}

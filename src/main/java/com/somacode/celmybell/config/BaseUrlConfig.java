package com.somacode.celmybell.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class BaseUrlConfig {
    /**
     * Esta clase es la encargada de conocer la base-url configurada en el application.yml
     * para enviarla concatenada en los recursos
     */
    public static String BASE_URL;
    public static String PROTOCOL;

    @Value("${custom.base-url}") String baseUrl;
    @Value("${custom.protocol}") String protocol;

    @PostConstruct
    public void init() {
        BASE_URL = baseUrl;
        PROTOCOL = protocol;
    }
}

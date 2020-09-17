package com.somacode.celmybell.service.tool;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.io.IOException;

@Service
public class PatchTool {

    private ObjectMapper objectMapper;

    @PostConstruct
    private void init() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }

    public void patch(Object source, Object target) {
        try {
            objectMapper.readerForUpdating(target).readValue(objectMapper.writeValueAsString(source));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

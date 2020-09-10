package com.somacode.celmybell.entity.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.io.IOException;

@Service
public class PatchTool {

    private ObjectMapper objectMapper;

    @PostConstruct
    private void init() {
        objectMapper = new ObjectMapper();
//        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }

    public void patch(Object source, Object target) {
        try {
            objectMapper.readerForUpdating(target).readValue(objectMapper.writeValueAsString(source));
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

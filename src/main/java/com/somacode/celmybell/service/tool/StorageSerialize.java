package com.somacode.celmybell.service.tool;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.somacode.celmybell.config.BaseUrlConfig;

import java.io.IOException;

public class StorageSerialize extends JsonSerializer<String> {

    private static final String REAL_STORAGE_PATH = BaseUrlConfig.PROTOCOL + "://" + BaseUrlConfig.BASE_URL + "/storage";

    @Override
    public void serialize(String string, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(REAL_STORAGE_PATH + string);
    }

}

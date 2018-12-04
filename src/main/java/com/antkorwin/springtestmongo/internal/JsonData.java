package com.antkorwin.springtestmongo.internal;


import com.antkorwin.commonutils.exceptions.InternalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class JsonData implements TextData {

    private final DataSet dataSet;
    private final ObjectMapper objectMapper;

    public JsonData(DataSet dataSet) {
        this.dataSet = dataSet;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public String read() {
        try {
            return objectMapper.writeValueAsString(dataSet.read());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InternalException(e);
        }
    }
}
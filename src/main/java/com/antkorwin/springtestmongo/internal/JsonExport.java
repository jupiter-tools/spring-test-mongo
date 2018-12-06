package com.antkorwin.springtestmongo.internal;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created on 06.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class JsonExport implements Text {

    private final DataSet dataSet;
    private final ObjectMapper objectMapper;

    public JsonExport(DataSet dataSet) {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.dataSet = dataSet;
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

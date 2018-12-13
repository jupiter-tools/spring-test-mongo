package com.antkorwin.springtestmongo.internal;

import com.antkorwin.commonutils.exceptions.InternalException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.antkorwin.springtestmongo.errorinfo.MongoDbErrorInfo.JSON_PARSING_ERROR;

/**
 * Convert a {@link Text} (in JSON format) to the {@link DataSet}
 *
 * @author Korovin Anatoliy
 */
class JsonImport implements DataSet {

    private final Text text;
    private final ObjectMapper objectMapper;

    JsonImport(Text text) {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.text = text;
    }

    @Override
    public Map<String, List<Map<String, Object>>> read() {

        String content = text.read();
        try {
            return objectMapper.readValue(content,
                                          new TypeReference<Map<String, List<Map<String, Object>>>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalException(JSON_PARSING_ERROR, e);
        }
    }
}
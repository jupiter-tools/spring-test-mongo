package com.antkorwin.springtestmongo.internal.expect;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Created on 08.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class ObjectMatcher {

    private final Map<String, Object> original;
    private final ObjectMapper objectMapper;

    public ObjectMatcher(Object original) {
        this.objectMapper = new ObjectMapper();
        this.original = convertToMap(original);
    }

    public boolean match(Object comparable) {

        Map<String, Object> comparableMap = convertToMap(comparable);

        for(String field : comparableMap.keySet()){
            if (!comparableMap.get(field)
                             .equals(original.get(field))) return false;
        }
        return true;
    }

    private Map<String, Object> convertToMap(Object object) {
        return objectMapper.convertValue(object, Map.class);
    }
}

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
    private final ComplexityDataTypes complexityDataTypes;

    public ObjectMatcher(Object original) {
        this.objectMapper = new ObjectMapper();
        this.original = convertToMap(original);
        this.complexityDataTypes = new ComplexityDataTypes();
    }

    public boolean match(Object comparable) {

        Map<String, Object> comparableMap = convertToMap(comparable);

        for (String field : comparableMap.keySet()) {

            Object originField = original.get(field);

            if (originField == null && comparableMap.get(field) != null) {
                return false;
            }
            if (complexityDataTypes.isComplexType(originField)) {
                boolean result = new ObjectMatcher(originField).match(comparableMap.get(field));
                if (!result) return false;
            } else {
                if (!comparableMap.get(field).equals(originField)) return false;
            }
        }
        return true;
    }

    private Map<String, Object> convertToMap(Object object) {
        return objectMapper.convertValue(object, Map.class);
    }
}

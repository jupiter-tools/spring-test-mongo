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

        for (Map.Entry<String, Object> comparableEntry : comparableMap.entrySet()) {

            String cmpKey = comparableEntry.getKey();
            Object cmpValue = comparableEntry.getValue();
            Object originValue = original.get(cmpKey);

            // Check cases with a null-value
            if (cmpValue == null) {
                continue;
            }
            if (originValue == null) {
                return false;
            }

            // Check the nested object (field by field)
            if (complexityDataTypes.isComplexType(originValue)) {
                if (!new ObjectMatcher(originValue).match(cmpValue)) {
                    return false;
                } else {
                    continue;
                }
            }

            // Check equals of values
            if (!originValue.equals(cmpValue)) {
                return false;
            }
        }

        return true;
    }

    private Map<String, Object> convertToMap(Object object) {
        return objectMapper.convertValue(object, Map.class);
    }
}

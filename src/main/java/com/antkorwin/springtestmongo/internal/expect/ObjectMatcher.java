package com.antkorwin.springtestmongo.internal.expect;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

/**
 * Created on 08.12.2018.
 *
 * @author Korovin Anatoliy
 */
public class ObjectMatcher {

    private final Map<String, Object> original;
    private final ObjectMapper objectMapper;
    private final WrapperDataTypes wrapperDataTypes;

    public ObjectMatcher(Object original) {
        this.objectMapper = new ObjectMapper();
        this.original = convertToMap(original);
        this.wrapperDataTypes = new WrapperDataTypes();
    }

    public boolean match(Object comparable) {

        Map<String, Object> comparableMap = convertToMap(comparable);

        for (String field : comparableMap.keySet()) {
            Object originField = original.get(field);

            if (originField == null && comparableMap.get(field) != null) {
                return false;
            }
            if (!wrapperDataTypes.isWrapperType(originField)) {
                boolean result = new ObjectMatcher(originField).match(comparableMap.get(field));
                if (!result) return false;
            }
            else {
                if (!comparableMap.get(field).equals(originField)) return false;
            }
        }
        return true;
    }

    private Map<String, Object> convertToMap(Object object) {
        return objectMapper.convertValue(object, Map.class);
    }

    class WrapperDataTypes {

        private final Set<Class> simpleTypes = Sets.newHashSet(Boolean.class,
                                                               Byte.class,
                                                               Short.class,
                                                               Character.class,
                                                               Integer.class,
                                                               Long.class,
                                                               Float.class,
                                                               Double.class,
                                                               Enum.class,
                                                               String.class);

        boolean isWrapperType(Object object) {
            Class<?> type = object.getClass();
            return !type.isPrimitive() && simpleTypes.contains(type);
        }
    }
}

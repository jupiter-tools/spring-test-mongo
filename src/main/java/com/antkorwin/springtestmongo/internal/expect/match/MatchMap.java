package com.antkorwin.springtestmongo.internal.expect.match;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created on 18.12.2018.
 *
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
public class MatchMap implements DataMatch {

    private final ObjectMapper objectMapper;

    public MatchMap() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public boolean match(Object original, Object expected) {

        Map<String, Object> originalMap = convertToMap(original);
        Map<String, Object> expectedMap = convertToMap(expected);

        for (Map.Entry<String, Object> expectedEntry : expectedMap.entrySet()) {

            Object expectedValue = expectedEntry.getValue();
            Object originValue = originalMap.get(expectedEntry.getKey());

            boolean matchResult = new MatchAny().match(originValue, expectedValue);
            if (!matchResult) return false;
        }

        return true;
    }

    private Map<String, Object> convertToMap(Object object) {
        return objectMapper.convertValue(object, Map.class);
    }
}

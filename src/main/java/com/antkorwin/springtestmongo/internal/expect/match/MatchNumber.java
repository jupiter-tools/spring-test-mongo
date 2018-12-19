package com.antkorwin.springtestmongo.internal.expect.match;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created on 19.12.2018.
 *
 * TODO: replace on javadoc
 *
 * @author Korovin Anatoliy
 */
public class MatchNumber implements MatchData {

    private final MatchObjects matchObjects;
    private final MatchLong matchLong;
    private final ObjectMapper objectMapper;

    public MatchNumber() {
        this.matchObjects = new MatchObjects();
        this.matchLong = new MatchLong();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public boolean match(Object original, Object expected) {

        JsonParser.NumberType numberType = objectMapper.valueToTree(expected).numberType();

        if (numberType == JsonParser.NumberType.INT || numberType == JsonParser.NumberType.LONG) {
            return matchLong.match(original, expected);
        }

        return matchObjects.match(original, expected);
    }
}
